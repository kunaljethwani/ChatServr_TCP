package server;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Server
{
	private final static Logger logger = Logger.getLogger(Server.class.getName());
	private static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	
	private static boolean configureServer(String[] args) throws FileNotFoundException
	{
		//FileInputStream f = new FileInputStream(new File("server.conf"));
		return true;
	}
	
	public static Logger getLogger()
	{
		return logger;
	}
	
	public static void submitThread(Thread thread)
	{
		threadPool.execute(thread);
	}
	

	public static void idle()
	{
		logger.info("Idle");
		logger.info("threadPool " + threadPool.toString());
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		// Configure and initialization
		boolean configured = false;
		ClientListener clientListener = ClientListener.instance();
		Thread clientListenerThread = null;
		
		try
		{
			configured = configureServer(args);
			clientListener.initialize(9094);
			
			clientListenerThread = new Thread(clientListener);
			threadPool.execute(clientListenerThread);
			
		} catch (FileNotFoundException e)
		{
			logger.severe(e.toString());
		}
		finally
		{
			if(!configured)
			{
				logger.severe("Failed to configure server, exiting...");
			}
		}
		
		System.out.println("Press any key to shutdown...");
		Scanner x = new Scanner(System.in);
		x.nextLine();
	
		System.out.println("Got a keypress on console. Shutting down...");
		clientListener.shutdown(); // shutdown client listener - clean exit for client listener
		
		threadPool.shutdown();
		threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS);

		x.close();
	}

}
