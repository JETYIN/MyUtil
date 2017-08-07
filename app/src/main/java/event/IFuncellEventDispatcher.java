package event;

public abstract interface IFuncellEventDispatcher
{
	public abstract boolean dispatch(Object... params);
	
	public abstract ThreadMode getThreadMode();
}

