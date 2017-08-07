package event;

import android.util.Log;

final class BackgroundPoster implements Runnable
{
	private final PendingPostQueue queue;
	private FuncellEventPublisher mPublisher;
	private volatile boolean mExecutorRunning;
	
	BackgroundPoster(FuncellEventPublisher publisher) {
		// TODO Auto-generated constructor stub
		this.mPublisher = publisher;
		this.queue = new PendingPostQueue();
	}
	
	void enqueue(IFuncellEventDispatcher dispatcher,Object... params){
		PendingPost pendingPost = PendingPost.obtainPendingPost(dispatcher,params);
		synchronized (this) {
			queue.enqueue(pendingPost);
			if (!mExecutorRunning) {
				mExecutorRunning = true;
				mPublisher.getExecutorService().execute(this);
            }
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
            try {
                while (true) {
                    PendingPost pendingPost = queue.poll(1000);
                    if (pendingPost == null) {
                        synchronized (this) {
                            // Check again, this time in synchronized
                            pendingPost = queue.poll();
                            if (pendingPost == null) {
                            	mExecutorRunning = false;
                                return;
                            }
                        }
                    }
                    mPublisher.invokeSubscriber(pendingPost);
                }
            } catch (InterruptedException e) {
                Log.w("Event", Thread.currentThread().getName() + " was interruppted", e);
            }
        } finally {
        	mExecutorRunning = false;
        }
	}
	
}

