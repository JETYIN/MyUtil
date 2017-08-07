package event;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Looper;
import android.util.Log;

/**
 * 当前event包用于事件分发机制、类似eventbus
 */
public abstract class FuncellBaseReceiver {
    static CopyOnWriteArrayList<WeakReference<FuncellBaseReceiver>> mContainer = new CopyOnWriteArrayList();
    protected Map<String, IFuncellEventDispatcher> mEvents = new ConcurrentHashMap();
    private String TAG = "FuncellBaseReceiver";
    private WeakReference<FuncellBaseReceiver> mThisWeakRef;
    private boolean mScaned;

    public FuncellBaseReceiver() {
        mThisWeakRef = new WeakReference(this);
        mContainer.add(0, mThisWeakRef);
    }

    private static List<Method> searchDeclaredMethods(Class<?> from, Class<?> end) {
        List<Method> list = null;
        for (Method[] methods = null; from != end; from = from.getSuperclass()) {
            methods = from.getDeclaredMethods();
            if ((methods != null) && (methods.length > 0)) {
                if (list == null) {
                    list = new ArrayList(methods.length);
                }
                list.addAll(Arrays.asList(methods));
            }
        }
//		return list == null ? Collections.emptyList() : list;
        return list;
    }

    static <T> void visit(Visitor<T> visitor, String eventID, Object... params) {
        Collection<WeakReference<FuncellBaseReceiver>> carbin = null;
        Iterator<WeakReference<FuncellBaseReceiver>> iter = mContainer.iterator();
        while (iter.hasNext()) {
            WeakReference<FuncellBaseReceiver> ref = iter.next();
            FuncellBaseReceiver target = ref.get();
            if (target == null) {
                if (carbin == null) {
                    carbin = new ArrayList();
                }
                carbin.add(ref);
            } else {
                visitor.visit((T) target, eventID, params);
            }
        }
        if (carbin != null) {
            mContainer.removeAll(carbin);
        }
    }

    public void detach() {
        mContainer.remove(mThisWeakRef);
    }

    void scanSubscriber(Object target) {
        List<Method> methods = searchDeclaredMethods(getClass(), FuncellBaseReceiver.class);
        if ((methods == null) || (methods.isEmpty())) {
            mScaned = true;
            return;
        }
        for (Method method : methods) {
            Subscribe subscribe = (Subscribe) method.getAnnotation(Subscribe.class);
            if (subscribe != null) {
                String[] eventIDs = subscribe.event();
                ThreadMode mode = subscribe.threadMode();
                IFuncellEventDispatcher dispatcher = new FuncellEventDispatcher(target, method, mode);
                for (String eventID : eventIDs) {
                    if (!mEvents.containsKey(eventID)) {
                        mEvents.put(eventID, dispatcher);
                    }
                }
            }
        }
        mScaned = true;
    }

    boolean hasScaned() {
        return mScaned;
    }

//	private void postToSubscrip(IFuncellEventDispatcher dispatcher,Object... params){
//		ThreadMode mode = dispatcher.getThreadMode();
//		PendingPost pendingPost = PendingPost.obtainPendingPost(dispatcher, params);
//		switch (mode) {
//		case MAIN:
//			if(Looper.getMainLooper() == Looper.myLooper()){
//				invokeSubscriber(pendingPost);
//			}else{
//				mMainThreadPoster.enqueue(dispatcher, params);
//			}
//			break;
//		case BACKGROUND:
//			
//			break;
//		case ASYNC:
//			
//			break;
//		default:
//			invokeSubscriber(pendingPost);
//			break;
//		}
//	}

//	void invokeSubscriber(PendingPost pendingPost) {
//		IFuncellEventDispatcher dispatcher = pendingPost.dispatcher;
//		Object[] params = pendingPost.params;
//		dispatcher.dispatch(params);
//		PendingPost.releasePendingPost(pendingPost);
//	}

    protected boolean handle(String eventID, Object... params) {
        IFuncellEventDispatcher dispatcher = (IFuncellEventDispatcher) mEvents.get(eventID);
        if (dispatcher == null) {
            return false;
        }
        //--------------------------
        /**
         * �ַ�����ͬ�Ķ�����
         */
//		postToSubscrip(dispatcher,params);

        //---------------------------

//		dispatcher.dispatch(params);
        return true;
    }

    static abstract interface Visitor<T> {
        public abstract void visit(T paramT, String paramString, Object... params);
    }
}
