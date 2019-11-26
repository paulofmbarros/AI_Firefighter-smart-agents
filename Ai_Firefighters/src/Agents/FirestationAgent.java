package Agents;

import Messages.FireMessage;
import Messages.OrderMessage;
import Messages.StatusMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import Config.Configurations;


public class FirestationAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ArrayList<ACLMessage>> firesToProcess;
	//private HashMap<String, ArrayList<StatusMessage>> firesAwaitingResponse;
	private HashMap<String, FireMessage> fireObjects;
	private int numberOfVehicles;
	//private ArrayList<WaterSource> waterSources; //perguntar ao worldmap onde estao as watersources
	//private ArrayList<FuelSource> fuelSources; //perguntar ao worldmap onde estao as fuelsources
	protected void setup() {
		this.firesToProcess = new HashMap<String, ArrayList<ACLMessage>>();
		this.fireObjects = new HashMap<String, FireMessage>();
		//this.firesAwaitingResponse = new HashMap<String, ArrayList<StatusMessage>>();
		this.numberOfVehicles = 0;
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new CountNumberOfVehicles(this));
		sb.addSubBehaviour(new ReceiveMessages(this));
		addBehaviour(sb);
	}
	
	
	class CountNumberOfVehicles extends OneShotBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public CountNumberOfVehicles(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ServiceDescription sd = new ServiceDescription();
			sd.setType("VEHICLE_AGENT");
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.addServices(sd);
			try {
				numberOfVehicles = DFService.search(myAgent, dfd).length;
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	

	class MessageAllVehicles extends OneShotBehaviour {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private FireMessage fm;
		public MessageAllVehicles(Agent a, FireMessage fm) {
			super(a);
			this.fm = fm;
		}

		@Override
		public void action() {
			ArrayList<ACLMessage> vehicles = new ArrayList<>();
			//se esta key ja existe, e porque foi de uma tentativa anterior e a lista deve ser ignorada
			firesToProcess.put(fm.getFireId(), vehicles);
			
			ServiceDescription sd = new ServiceDescription();
			sd.setType("VEHICLE_AGENT");
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.addServices(sd);
			
			try {
				DFAgentDescription[] dfds = DFService.search(myAgent, dfd);
				if (dfds.length > 0) {
					System.out.println("Localized vehicles");
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					for (DFAgentDescription dfad : dfds) {
						msg.addReceiver(dfad.getName());
					}
					msg.setContentObject(fm);
					send(msg);
				} else {
					System.out.println("Couldn't localize vehicles!");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Failed searching in the DF!");
			}
		}
	}
	
	class SendBestVehicle extends OneShotBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String fireId;
		
		public SendBestVehicle(Agent a, String fireId) {
			super(a);
			this.fireId = fireId;
		}
		@Override
		public void action() {
			//1: extrair os estados do veiculo a partir da lista de mensagens
			//associada ao ID do fogo na hashtable
			//2: criar uma lista de veiculos que NAO estao ocupados
			//3: dessa lista, escolher o veiculo mais proximo (em tempo) do fogo
			//3.1: se tem combustivel* e agua, o tempo e o de viagem ao fogo
			//3.2: se tem combustivel* mas nao tem agua, o tempo é o tempo de viagem ao posto de agua mais proximo e daí, o tempo que chega ao fogo
			//3.3: se não tem nem combustível nem água, o tempo é medido como a soma da viagem à bomba, ao posto de água, e do posto de água, ao fogo
			//* : não ter combustível significa não ter combustível para viagar do sítio em que está até ao fogo e do fogo à bomba mais próxima.
			//**: se não tiver água, não esquecer que o combustível é gasto também durante a viagem ao posto de água.
			System.out.println("For fire id " + fireId + " I can choose from " + firesToProcess.get(fireId).size() + " vehicles");
			ArrayList<StatusMessage> statuses = new ArrayList<StatusMessage>();
			for(ACLMessage msg : firesToProcess.get(fireId)) {
				try {
					statuses.add((StatusMessage) msg.getContentObject());
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
			//preciso de usar iterator para remover elementos do arraylist dentro do proprio loop
			//isto vai dar jeito para filtrar veiculos nao disponiveis
			Iterator<StatusMessage> i = statuses.iterator();
			while (i.hasNext()) {
			   StatusMessage msg = ((StatusMessage) i.next());
			   if(!msg.isAvailable()) {
				   i.remove();
			   }
			}
			if(statuses.size() == 0) {
				//nenhum encontrado, fazer qqr coisa sobre isto
				//TODO IMPORTANTE. A firestation deve voltar a tentar encontrar veiculos
				System.out.println("But none of those were available...");
				try {
					Thread.sleep(3000);
				}
				catch(Exception e ) {
					System.out.println(e);
				}
				addBehaviour(new MessageAllVehicles(myAgent, fireObjects.get(fireId)));
			}
			else {				
				//firesAwaitingResponse.put(fireId, statuses);
				StatusMessage best = selectBestStatus(fireId, statuses);
				if(best == null) {
					//nenhum veiculo pode ir apagar o fogo...
					System.out.println("There are vehicles available but none can travel to fire (no fuel/water?)");
					try {
						Thread.sleep(3000);
					}
					catch(Exception e ) {
						System.out.println(e);
					}
					addBehaviour(new MessageAllVehicles(myAgent, fireObjects.get(fireId)));
				}
				else {
					OrderMessage om = new OrderMessage(fireObjects.get(fireId).getFireCoordX(), fireObjects.get(fireId).getFireCoordY(), fireId);
					ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
					try {
						msg.setContentObject(om);
						msg.addReceiver(best.getVehicleName());
						send(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	
	public int simulateDistance(int x1, int y1, int x2, int y2) {
		int moves = 0;
		while(x1 != x2 || y1 != y2) {
			if (x1 < x2) {
				x1++;
			} else if (x1 > x2) {
				x1--;
			}
			if (y1 < y2) {
				y1++;
			} else if (y1 > y2) {
				y1--;
			}
			moves++;
		}
		return moves;
	}
	
	public StatusMessage selectBestStatus(String fireId, ArrayList<StatusMessage> statuses) {
		StatusMessage best = null;
		double bestTime = 99999;
		FireMessage fm = fireObjects.get(fireId);
		
		for(StatusMessage sm : statuses) {
			float speed = 9999;
			double time = 9999;
			boolean needsWater = false;
			boolean needsFuel = false;
			if(sm.getVehicleType() == "FIRETRUCK") {
				speed = Configurations.FIRE_TRUCK_SPEED_MULTIPLIER * Configurations.BASE_VEHICLE_SPEED;
			}
			else if (sm.getVehicleType() == "DRONE") {
				speed = Configurations.DRONE_SPEED_MULTIPLIER * Configurations.BASE_VEHICLE_SPEED;
			}
			else {
				speed = Configurations.AIRPLANE_SPEED_MULTIPLIER * Configurations.BASE_VEHICLE_SPEED;
			}
			
			if(speed == 9999) {
				System.out.println("Something went wrong fetching vehicle speed. Using default value (9999)");
			}
			
			needsWater = sm.getWaterTank() <= 0;
			if(needsWater) {
				//escolhi 30 30 como as coords temporarias da bomba de agua
				needsFuel = simulateDistance(sm.getCoordX(), sm.getCoordY(), 30, 30) + simulateDistance(30, 30, fm.getFireCoordX(), fm.getFireCoordY()) < sm.getFuelTank();
			}
			else {
				needsFuel = simulateDistance(sm.getCoordX(), sm.getCoordY(), fm.getFireCoordX(), fm.getFireCoordY()) < sm.getFuelTank();
			}
			
			int totalDistance = 9999;
			
			if(needsFuel && !needsWater) {
				//50 50 sao as coords da estacao dos bombeiros, meter isto dinamico mais para a frente
				totalDistance = simulateDistance(sm.getCoordX(), sm.getCoordY(), 50, 50);
				totalDistance += simulateDistance(50, 50, fm.getFireCoordX(), fm.getFireCoordY());
			}
			
			else if(needsWater && needsFuel) {
				totalDistance = simulateDistance(sm.getCoordX(), sm.getCoordY(), 50, 50);
				totalDistance += simulateDistance(50, 50, 30, 30);
				totalDistance += simulateDistance(30, 30, fm.getFireCoordX(), fm.getFireCoordY());
			}
			else if (needsWater && !needsFuel) {
				totalDistance = simulateDistance(sm.getCoordX(), sm.getCoordY(), 30, 30);
				totalDistance += simulateDistance(30, 30, fm.getFireCoordX(), fm.getFireCoordY());
			}
			
			time = speed * totalDistance;
			if(time < bestTime) {
				bestTime = time;
				best = sm;
			}
			
		}
		
		return best;
	}
	
	class ReceiveMessages extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ReceiveMessages(Agent a) {
			super(a);
		}

		
		public void action() {
			ACLMessage msg = receive();
			if(msg == null) {
				block();
				return;
			}
			
			try {
				Object content = msg.getContentObject();
				switch(msg.getPerformative()) {
				case (ACLMessage.INFORM):
					if(content instanceof FireMessage) {
						FireMessage fm = (FireMessage) content;
						System.out.println("Fire msg received. Checking all vehicles...");
						fireObjects.put(fm.getFireId(), fm);
						addBehaviour(new MessageAllVehicles(myAgent, fm));
					}
					else if(content instanceof StatusMessage) {
						
						StatusMessage sm = (StatusMessage) content;
						firesToProcess.get(sm.getFireId()).add(msg);
						if(firesToProcess.get(sm.getFireId()).size() == numberOfVehicles) {
							addBehaviour(new SendBestVehicle(myAgent, sm.getFireId()));
						}
					}
					break;
				case (ACLMessage.REJECT_PROPOSAL):
					System.out.println("Vehicle wasn't available, redoing the search...");
					FireMessage fm = fireObjects.get(((StatusMessage) content).getFireId());
					System.out.println("The fire id was " + fm.getFireId());
					addBehaviour(new MessageAllVehicles(myAgent, fm));
					break;
				case (ACLMessage.ACCEPT_PROPOSAL):
					break;
				case (ACLMessage.CONFIRM):
					OrderMessage om = (OrderMessage) msg.getContentObject();
					firesToProcess.remove(om.getFireId());
					fireObjects.remove(om.getFireId());
					
					//AQUI ENVIA A MENSAGEM AO WORLDAGENT PARA REMOVER O FOGO DO WORLD
					ACLMessage message = new ACLMessage(ACLMessage.CONFIRM);
					message.setContentObject(om);
					message.addReceiver(new AID("WorldAgent", AID.ISLOCALNAME));
					send(message);
					
					System.out.println("There are still " + fireObjects.size() + " (" + firesToProcess.size() + ")" + " fires to process");
					break;
				default:
					break;
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
	}
}
