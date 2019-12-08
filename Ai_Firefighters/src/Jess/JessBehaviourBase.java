package Jess;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public abstract class JessBehaviourBase extends Behaviour {

    protected boolean done = false;
	private static final long serialVersionUID = 1L;

	public JessBehaviourBase(Agent agent) {
		super(agent);
	}

	protected ArrayList<AID> findAgentsOfType(String type) {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		template.addServices(sd);
		ArrayList<AID> agents = new ArrayList<AID>();

		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			System.out.println(type + " agents:");
			for (int i = 0; i < result.length; ++i) {
				agents.add(result[i].getName());
				System.out.println(agents.get(i).getName());
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		return agents;
	}


	@Override
	public boolean done() {
		return done;
	}
}
