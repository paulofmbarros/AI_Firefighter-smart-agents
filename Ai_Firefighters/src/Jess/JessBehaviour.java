package Jess;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import jade.core.Agent;
import jess.Jesp;
import jess.JessException;
import jess.Rete;
import jess.ValueVector;

public class JessBehaviour extends JessBehaviourBase {
	// the Jess engine
	private jess.Rete jess;
	// maximum number of passes that a run of Jess can execute before giving
	// control to the agent
	private static final int MAX_JESS_PASSES = 100;

	public JessBehaviour(Agent agent, String jessFile){
		super(agent);
		System.out.println("Working directory: " + System.getProperty("user.dir"));
        jess = new Rete();
		try{
			FileReader fr = new FileReader(jessFile);
			Jesp jessParser = new Jesp(fr, jess);
			try{
				jessParser.parse(false);
                jess.reset();
            }
			catch(JessException je){
				je.printStackTrace();
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			System.err.println("Error loading jess file!");
		}
	}
	
	@Override
	public void action() {
		int executedPasses = -1;
		try{
            executedPasses = jess.run(MAX_JESS_PASSES);
			jess.executeCommand("(facts)");
        }
		catch(JessException je){
			je.printStackTrace();
		}
		if(executedPasses < MAX_JESS_PASSES){
			block();
		}

	}
	


	public boolean addFact(String jessFact){
		try{
			jess.assertString(jessFact);
		}
		catch(JessException je){
			je.printStackTrace();
			return false;
		}

        if(!isRunnable()) restart();

        return true;
	}

    public Iterator runQuery(String queryName, ValueVector values){
        Iterator it = null;
        try{
            it = jess.runQuery(queryName, values);
        }
        catch(JessException je){
            je.printStackTrace();
        }
        return it;
    }


    public void removeFacts(String name) {
        try {
            jess.removeFacts(name);
        } catch (JessException e) {
            e.printStackTrace();
        }
    }
}
