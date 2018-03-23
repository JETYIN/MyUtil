package utils.ok_re;

public class FunHttpManager {

	private static final RequestInstance FACTORY;

	static {
		FACTORY = new RequestInstance();// retrofit，也可以设置为volly
	}

	/**
	 * 创建对象
	 * 
	 * @return
	 */
	public static IFunRequest with() {// RetrofitManager实现了IFunRequest接口
		return FACTORY.create();

	}

	private interface RequestFactory {
		IFunRequest create();
	}

	private static class RequestInstance implements RequestFactory {
		@Override
		public IFunRequest create() {
			return RetrofitManager.getInstance();
		}
	}

}
