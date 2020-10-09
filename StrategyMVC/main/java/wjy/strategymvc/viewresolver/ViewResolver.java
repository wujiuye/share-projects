package wjy.strategymvc.viewresolver;

public interface ViewResolver {

	View resolveViewName(String viewName) throws Exception;
}
