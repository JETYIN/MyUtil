package event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Looper;


public class FuncellEventPublisher implements IFuncellEventPublisher
{
	
	private static FuncellEventPublisher mInstance;
	private List<FuncellSDKEventReceiver> mReceivers;
	private final HandlerPoster mMainThreadPoster;
	private final BackgroundPoster mBackgroundPoster;
	private final AsyncPoster mAsyncPoster;
	private final ExecutorService mExecutorService;
	
	public static FuncellEventPublisher getInstance() {
		if (mInstance == null) {
			synchronized (FuncellEventPublisher.class) {
				if (mInstance == null) {
					mInstance = new FuncellEventPublisher();
				}
			}
		}
		return mInstance;
	}
	
	FuncellEventPublisher(){
		mReceivers = Collections.synchronizedList(new ArrayList());
		mMainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
		mBackgroundPoster = new BackgroundPoster(this);
		mAsyncPoster = new AsyncPoster(this);
		mExecutorService = Executors.newCachedThreadPool();
	}
	
	ExecutorService getExecutorService() {
        return mExecutorService;
    }
	
	public void register(FuncellSDKEventReceiver eventReceiver) {
		if (eventReceiver != null) {
			mReceivers.add(eventReceiver);
		}
	}
	
	public void unregister(FuncellSDKEventReceiver eventReceiver) {
		if (eventReceiver != null) {
			mReceivers.remove(eventReceiver);
			eventReceiver.detach();
		}
	}
	
	@Override
	public void publish(String eventID, Object... params) {
		// TODO Auto-generated method stub
		FuncellBaseReceiver.visit(mVisitor, eventID, params);
	}
	
	private FuncellBaseReceiver.Visitor<FuncellBaseReceiver> mVisitor = new FuncellBaseReceiver.Visitor<FuncellBaseReceiver>() {

		@Override
		public void visit(FuncellBaseReceiver receiver, String eventID,Object... params) {
			// TODO Auto-generated method stub
			if (!receiver.hasScaned()) {
				receiver.scanSubscriber(receiver);
			}
			IFuncellEventDispatcher dispatcher = (IFuncellEventDispatcher)receiver.mEvents.get(eventID);
			if (dispatcher == null) {
				return;
			}
			postToSubscrip(dispatcher,params);
//			receiver.handle(eventID, params);
		}
	};
	
	private void postToSubscrip(IFuncellEventDispatcher dispatcher,Object... params){
		ThreadMode mode = dispatcher.getThreadMode();
		PendingPost pendingPost = PendingPost.obtainPendingPost(dispatcher, params);
		switch (mode) {
		case MAIN:
			if(Looper.getMainLooper() == Looper.myLooper()){
				invokeSubscriber(pendingPost);
			}else{
				mMainThreadPoster.enqueue(dispatcher, params);
			}
			break;
		case BACKGROUND:
			if(Looper.getMainLooper() == Looper.myLooper()){
				mBackgroundPoster.enqueue(dispatcher, params);
			}else {
				invokeSubscriber(pendingPost);
			}
			break;
		case ASYNC:
			mAsyncPoster.enqueue(dispatcher, params);
			break;
		default:
			invokeSubscriber(pendingPost);
			break;
		}
	}
	
	void invokeSubscriber(PendingPost pendingPost) {
		IFuncellEventDispatcher dispatcher = pendingPost.dispatcher;
		Object[] params = pendingPost.params;
		dispatcher.dispatch(params);
		PendingPost.releasePendingPost(pendingPost);
	}
}

