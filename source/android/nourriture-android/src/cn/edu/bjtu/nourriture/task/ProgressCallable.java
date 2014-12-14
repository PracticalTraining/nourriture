package cn.edu.bjtu.nourriture.task;



public interface ProgressCallable<T> {
    public T call(final IProgressListener pProgressListener) throws Exception;
}
