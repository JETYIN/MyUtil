package event;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

final class PendingPost {
	private final static List<PendingPost> pendingPostPool = new ArrayList<PendingPost>();
	PendingPost next;
	IFuncellEventDispatcher dispatcher;
	Object[] params;
	
	private PendingPost(IFuncellEventDispatcher dispatcher,Object... params) {
		// TODO Auto-generated constructor stub
		this.dispatcher = dispatcher;
		this.params = params;
	}
	
	static PendingPost obtainPendingPost(IFuncellEventDispatcher dispatcher,Object... params) {
		synchronized (pendingPostPool) {
			int size = pendingPostPool.size();
			if (size > 0) {
                PendingPost pendingPost = pendingPostPool.remove(size - 1);
                pendingPost.dispatcher = dispatcher;
                pendingPost.params = params;
                pendingPost.next = null;
                return pendingPost;
            }
		}
		return new PendingPost(dispatcher,params);
	}
	
	static void releasePendingPost(PendingPost pendingPost) {
        pendingPost.dispatcher = null;
        pendingPost.next = null;
        pendingPost.params = null;
        synchronized (pendingPostPool) {
            // Don't let the pool grow indefinitely
            if (pendingPostPool.size() < 10000) {
                pendingPostPool.add(pendingPost);
            }
        }
    }
}