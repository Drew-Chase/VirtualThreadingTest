import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class VirtualThreadTest
{
    public static void main(String[] args)
    {
        String[] array = new String[100];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = "Element " + i;
        }
        AtomicInteger count = new AtomicInteger();
        int currentNumberOfThreads = Runtime.getRuntime().availableProcessors();
        parallelForeach(array, currentNumberOfThreads, (element) ->
        {
            System.out.printf("Processing: %s%n", element);
            count.getAndIncrement();
        });
        System.out.printf("Total elements processed: %d%n", count.get());
    }

    /**
     * Executes the specified action on the elements of the array in parallel using multiple threads.
     *
     * @param <T>     the type of elements in the array
     * @param array   the array to operate on
     * @param threads the number of threads to use for parallel processing
     * @param action  the action to be performed on each element
     */
    public static <T> void parallelForeach(T[] array, int threads, Consumer<T> action)
    {
        if (threads > array.length) // Limit the number of threads to the size of the array
            threads = array.length;

        // split the array into x chunks of equal size where x = threads
        int chunkSize = array.length / threads;

        // Create an array of virtual threads
        Thread[] virtualThreads = new Thread[threads];

        // Loop through the array and create a virtual thread for each chunk
        for (int i = 0; i < threads; i++)
        {

            // Calculate the start and end index of the chunk
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize;
            if (i == threads - 1)
            {
                end = array.length;
            }
            int finalEnd = end;
            virtualThreads[i] = Thread.ofVirtual().start(() ->
            {
                // Process the chunk
                for (int j = start; j < finalEnd; j++)
                {
                    action.accept(array[j]);
                }
            });
        }

        // Wait for all virtual threads to finish
        for (Thread virtualThread : virtualThreads)
        {
            try
            {
                virtualThread.join();
            } catch (InterruptedException e)
            {
                System.err.printf("Thread %s was interrupted: %s%n", virtualThread.getName(), e.getMessage());
            }
        }

    }

}