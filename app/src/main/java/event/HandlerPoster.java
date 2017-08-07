package event;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

final class HandlerPoster extends Handler
{
	private final int maxMillisInsideHandleMessage;
	private boolean handlerActive;
	private final PendingPostQueue queue;
	private FuncellEventPublisher mPublisher;
	
	HandlerPoster(FuncellEventPublisher publisher,Looper looper,int maxMillisInsideHandleMessage) {
		// TODO Auto-generated constructor stub
		super(looper);
		this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
		this.mPublisher = publisher;
		this.queue = new PendingPostQueue();
	}
	
	void enqueue(IFuncellEventDispatcher dispatcher,Object... params){
		PendingPost pendingPost = PendingPost.obtainPendingPost(dispatcher,params);
		synchronized (this) {
			queue.enqueue(pendingPost);
			if (!handlerActive) {
                handlerActive = true;
                if (!sendMessage(obtainMessage())) {
                    throw new FuncellEventException("Could not send handler message");
                }
            }
		}
	}
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                PendingPost pendingPost = queue.poll();
                if (pendingPost == null) {
                    synchronized (this) {
                        // Check again, this time in synchronized
                        pendingPost = queue.poll();
                        if (pendingPost == null) {
                            handlerActive = false;
                            return;
                        }
                    }
                }
                mPublisher.invokeSubscriber(pendingPost);
                long timeInMethod = SystemClock.uptimeMillis() - started;
                if (timeInMethod >= maxMillisInsideHandleMessage) {
                    if (!sendMessage(obtainMessage())) {
//                        throw new FuncellEventException("Could not send handler message");
                    	Log.w("Event", "Could not send handler message");
                    }
                    rescheduled = true;
                    return;
                }
            }
        } finally {
            handlerActive = rescheduled;
        }
	}
	
}

