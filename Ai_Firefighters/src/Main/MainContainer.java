package Main;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

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

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		a.initMainContainerInPlatform("localhost", "9090", "MainContainer");
		
		// Name of the Agent + Class Path of Agent's source Code
		
		a.startAgentInPlatform("WorldAgent", "Agents.WorldAgent");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		a.startAgentInPlatform("Firetruck1", "Agents.Vehicles_firetruckAgent");
		a.startAgentInPlatform("Drone1", "Agents.Vehicles_droneAgent");
		a.startAgentInPlatform("Aircraft1", "Agents.Vehicles_aircraftAgent");
		
		a.startAgentInPlatform("Firetruck2", "Agents.Vehicles_firetruckAgent");
		a.startAgentInPlatform("Drone2", "Agents.Vehicles_droneAgent");
		a.startAgentInPlatform("Aircraft2", "Agents.Vehicles_aircraftAgent");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		a.startAgentInPlatform("FirestationAgent", "Agents.FirestationAgent");
		a.startAgentInPlatform("VehiclesAgent", "Agents.VehiclesAgent");
		a.startAgentInPlatform("Interface", "Agents.InterfaceAgent");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		int counter = 0;
		while(true) {
			String fireName = "FireStarterAgent" + counter;
			counter++;
			a.startAgentInPlatform(fireName, "Agents.FireStarterAgent");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		a.startAgentInPlatform("FireStarterAgent", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent2", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent3", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent4", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent5", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent6", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent7", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent8", "Agents.FireStarterAgent");
//		a.startAgentInPlatform("FireStarterAgent9", "Agents.FireStarterAgent");
	}

}
