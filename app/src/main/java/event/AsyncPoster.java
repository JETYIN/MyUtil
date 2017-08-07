package event;

import android.util.Log;

final class AsyncPoster implements Runnable
{
	private final PendingPostQueue queue;
	private FuncellEventPublisher mPublisher;
	
	AsyncPoster(FuncellEventPublisher publisher) {
		// TODO Auto-generated constructor stub
		this.mPublisher = publisher;
		this.queue = new PendingPostQueue();
	}
	
	void enqueue(IFuncellEventDispatcher dispatcher,Object... params){
		PendingPost pendingPost = PendingPost.obtainPendingPost(dispatcher,params);
		queue.enqueue(pendingPost);
		mPublisher.getExecutorService().execute(this);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		PendingPost pendingPost = queue.poll();
        if(pendingPost == null) {
//            throw new IllegalStateException("No pending post available");
        	Log.w("Event", "No pending post available");
        	return;
        }
        mPublisher.invokeSubscriber(pendingPost);
	}
	
}

