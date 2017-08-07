package event;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FuncellEventDispatcher implements IFuncellEventDispatcher
{
	private WeakReference<Object> FuncellEventDispatcher;
	private Method mMethod;
	private boolean mIsStatic;
	private boolean mParameterTypes;
	private ThreadMode mMode;
	
	FuncellEventDispatcher(Object target, Method method,ThreadMode mode){
		FuncellEventDispatcher = new WeakReference(target);
		mMethod = method;
		mMethod.setAccessible(true);
		mIsStatic = Modifier.isStatic(mMethod.getModifiers());
	    Class<?>[] parameterTypes = mMethod.getParameterTypes();
	    mParameterTypes = ((parameterTypes == null) || (parameterTypes.length == 0));
	    mMode = mode;
	}
	
	@Override
	public boolean dispatch(Object... params) {
		// TODO Auto-generated method stub
		boolean ret = false;
		try {
			if (mIsStatic) {
				if (mParameterTypes) {
					mMethod.invoke(null);
				} else {
					mMethod.invoke(null, params);
				}
				ret = true;
			} else if (FuncellEventDispatcher.get() != null) {
				if (this.mParameterTypes) {
					mMethod.invoke(FuncellEventDispatcher.get());
				} else {
					mMethod.invoke(FuncellEventDispatcher.get(), params);
				}
				ret = true;
			}
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		return ret;
	}

	@Override
	public ThreadMode getThreadMode() {
		// TODO Auto-generated method stub
		return mMode;
	}
}

