# Virtual Threading Test
This is a test of virtual threading in Java 21.   
## Explanation
In Java 21 you can create a virtual thread like this
```java
Thread.ofVirtual().start(() ->
{
    // Code here
});
```
To run multiple virtual threads you can use a loop like this
```java
for (int i = 0; i < 10; i++)
{
    Thread.ofVirtual().start(() ->
    {
        // Code here
    });
}
```

and to wait for them all to complete before continuing you can use a loop like this
```java
int size = 10;
Thread[] threads = new Thread[size];
for (int i = 0; i < size; i++)
{
    threads[i] = Thread.ofVirtual().start(() ->
    {
        // Code here
    });
}
for (Thread virtualThread : threads)
{
    try
    {
        virtualThread.join();
    } catch (InterruptedException e)
    {
        System.err.printf("Thread %s was interrupted: %s%n", virtualThread.getName(), e.getMessage());
    }
}
```

A full example can be viewed in the [VirtualThreadTest.java](src/VirtualThreadTest.java) file.