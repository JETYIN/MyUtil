package http.dagger2.component;

import dagger.Component;
import work.dylan.com.rxdemo.dagger2.test.ProductActivity;

/**
 * Created by Administrator on 2018/6/26.
 */
@Component(dependencies = AppComponent.class)
public interface ProductComponent {
    ProductActivity inject(ProductActivity activity);
}
