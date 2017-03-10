package utils.versionandhandware;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;

/**
 * 获取资源的ID,生成自己sdk时，资源文件打包到jar中存在不能使用的问题，需要采用下面的方法进行处理
 *
 */
public class RUtils {

	public static int layout(Context paramContext, String paramString) {
		Log.e("RUtils", " package name is "+paramContext.getPackageName());
		return paramContext.getResources().getIdentifier(paramString, "layout",
				paramContext.getPackageName());
	}

	public static int string(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string",
				paramContext.getPackageName());
	}

	public static int drawable(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
	}

	public static int style(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style",
				paramContext.getPackageName());
	}

	public static int id(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id",
				paramContext.getPackageName());
	}

	public static int color(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color",
				paramContext.getPackageName());
	}

	public static int anim(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "anim",
				paramContext.getPackageName());
	}

	public static int attr(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "attr",
				paramContext.getPackageName());
	}
	
	public static int raw(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "raw",
				paramContext.getPackageName());
	}

	public static int styleable(Context paramContext, String paramString) {
		
		return (Integer) getResourceId(paramContext, paramString, "styleable");
	}

	public static int[] styleableArray(Context paramContext, String paramString) {
		
		return (int[]) getResourceId(paramContext, paramString, "styleable");
	}

	/**
	 * 
	 * 对于 context.getResources().getIdentifier 无法获取的数据 , 或者数组
	 * 
	 * 资源反射值
	 * 
	 * @paramcontext
	 * 
	 * @param name
	 * 
	 * @param type
	 * 
	 * @return
	 */

	private static Object getResourceId(Context context, String name,
			String type) {

		String className = context.getPackageName() + ".R";

		try {

			Class<?> cls = Class.forName(className);

			for (Class<?> childClass : cls.getClasses()) {

				String simple = childClass.getSimpleName();

				if (simple.equals(type)) {

					for (Field field : childClass.getFields()) {

						String fieldName = field.getName();
						

						if (fieldName.equals(name)) {
							
							
							return field.get(null);

						}

					}

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}
}
