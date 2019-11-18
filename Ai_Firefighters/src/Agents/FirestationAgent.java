package Agents;

import Messages.FireMessage;
import Messages.StatusMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class FirestationAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ArrayList<ACLMessage>> firesToProcess;
	private HashMap<String, FireMessage> fireObjects;
	private int numberOfVehicles;
	//private ArrayList<WaterSource> waterSources; //perguntar ao worldmap onde estao as watersources
	//private ArrayList<FuelSource> fuelSources; //perguntar ao worldmap onde estao as fuelsources
	protected void setup() {
		this.firesToProcess = new HashMap<String, ArrayList<ACLMessage>>();
		this.fireObjects = new HashMap<String, FireMessage>();
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
			   // Do something
			   System.out.println(msg.getVehicleName());
			}
		}
		
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
