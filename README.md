# sql-batch-executor
Request database by defining jobs in or out transaction


```java
    public void testInTransactionOkInContainer() throws Exception {
        SqlBatchExecutor sqlExecutor = new SqlBatchExecutor(TEST_DB_URL, true);

        JobContainer jobContainer = new JobContainer();
        jobContainer.add(new AddUserJob(createUser("john", "doe")));
        jobContainer.add(new AddUserJob(createUser("jane", "doe")));

        sqlExecutor.update(jobContainer);

        assertEquals(2, count());
    }
```   

Some other examples :

[BatchExecutorTest.java](./src/test/java/sqlexecutor/BatchExecutorTest.java)
