package nf.framework.core.http;

public  abstract  class SimpleUiRequestHandler<T> implements AbsUIResquestHandler<T> {

	@Override
	public void onPreExcute(AbsBaseRequestData<?> baseRequestData) {
	}
	@Override
	public void onSuccessPostExecute(AbsBaseRequestData<?> baseRequestData,
			T object, boolean hasNext) {
		onSuccessExcute(baseRequestData,object,hasNext);
	}
	@Override
	public void onFailurePostExecute(AbsBaseRequestData<?> baseRequestData,
			String failureMsg) {
	}
	@Override
	public void onCompleteExcute(AbsBaseRequestData<?> baseRequestData) {
	}
	
	public abstract void onSuccessExcute(AbsBaseRequestData<?> baseRequestData,
			T obj, boolean hasNext);
}
