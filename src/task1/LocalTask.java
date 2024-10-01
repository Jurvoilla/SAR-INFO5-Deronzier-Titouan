package task1;

class LocalTask extends Task {

    public LocalTask(Broker broker, Runnable task) {
        super(broker, task);
    }

    @Override
    public void run() {
        this.runnable.run();
    }
    
//    public void store() {
//        long runnableID = Thread.currentThread().getId();
//    	Task.storeThread(runnableID, task);
//    }
//    
//    public void delete() {
//    	long runnableID = Thread.currentThread().getId();
//    	Task.deleteThread(runnableID, task);
//    }
}



