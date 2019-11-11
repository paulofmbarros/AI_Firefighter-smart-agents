package Main;
import jade.core.Runtime;
import jade.core.ContainerID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class MainContainer {
	Runtime rt;
	ContainerController container;

	public ContainerController initContainerInPlatform(String host, String port, String containerName) {
		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, containerName);
		profile.setParameter(Profile.MAIN_HOST, host);
		profile.setParameter(Profile.MAIN_PORT, port);
		// create a non-main agent container
		ContainerController container = rt.createAgentContainer(profile);
		return container;
	}

	public void initMainContainerInPlatform(String host, String port, String containerName) {

		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile prof = new ProfileImpl();
		prof.setParameter(Profile.CONTAINER_NAME, containerName);
		prof.setParameter(Profile.MAIN_HOST, host);
		prof.setParameter(Profile.MAIN_PORT, port);
		prof.setParameter(Profile.MAIN, "true");
		prof.setParameter(Profile.GUI, "true");

		// create a main agent container
		this.container = rt.createMainContainer(prof);
		rt.setCloseVM(true);

	}

	public void startAgentInPlatform(String name, String classpath) {
		try {
			AgentController ac = container.createNewAgent(name, classpath, new Object[0]);
			ac.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MainContainer a = new MainContainer();
		
		a.initMainContainerInPlatform("localhost", "9888", "MainContainer");
		//a.startAgentInPlatform("Seller", "Agents.Seller");
		
		// Example of Container Creation(Not main Controller)
		ContainerController newcontainer1 = a.initContainerInPlatform("localhost", "9888", "Container1");
		ContainerController newcontainer2 = a.initContainerInPlatform("localhost", "9888", "Container2");
		ContainerController newcontainer3 = a.initContainerInPlatform("localhost", "9888", "Container3");

		//Example of Agent Creation in new container
		try {
			//Start seller1, seller2 and seller3 in container 1,2,3
			AgentController seller1 = newcontainer1.createNewAgent("Seller1", "Agents.Seller", new Object[0]);
			AgentController seller2 = newcontainer2.createNewAgent("Seller2", "Agents.Seller", new Object[0]);
			AgentController seller3 = newcontainer3.createNewAgent("Seller3", "Agents.Seller", new Object[0]);
			
			seller1.start();
			seller2.start();
			seller3.start();
		
			int n = 0;
			int limit = 100; // Limit number of Customers
			try {
				while (n<limit) {   //novo Costumer a cada segundo até ter 10 Costumers
					AgentController customer1 = newcontainer1.createNewAgent("Customer1", "Agents.Customer", new Object[0]);
					AgentController customer2 = newcontainer2.createNewAgent("Customer2", "Agents.Customer", new Object[0]);
					AgentController customer3 = newcontainer3.createNewAgent("Customer3", "Agents.Customer", new Object[0]);
					customer1.start();
					customer2.start();
					customer3.start();
					n++;
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		/*
		// Example of Container Creation (not the main container)
		ContainerController newcontainer = a.initContainerInPlatform("localhost", "9888", "OtherContainer");
		
		// Example of Agent Creation in new container
		try {
			AgentController ag = newcontainer.createNewAgent("agentnick", "ReceiverAgent", new Object[] {});// arguments
			ag.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		*/
	}

}
