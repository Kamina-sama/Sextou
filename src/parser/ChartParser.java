package parser;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import gameobject.Arrow;
import gameobject.Direction;

import org.json.simple.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;

public class ChartParser {
	
	public double speed;
	static double speed_dampening=1.2;
	
	private class SectionArrows {
		public Vector<OrphanArrow> BFs;
		public Vector<OrphanArrow> opponent;
		public SectionArrows(Vector<OrphanArrow> v1, Vector<OrphanArrow> v2) {
			BFs=v1;
			opponent=v2;
		}
		//(double speed, double start, int player, double duration, Direction direction)
		public Vector<Arrow> MergeComplete() {
			Vector<Arrow> result=new Vector<Arrow>();
			for(OrphanArrow a: BFs) {
				result.add(new Arrow(a.speed, a.position, 1, a.duration, Direction.values()[(int) (a.column%4)]));
			}
			for(OrphanArrow b: opponent) {
				result.add(new Arrow(b.speed, b.position, 0, b.duration, Direction.values()[(int) (b.column%4)]));
			}
			return result;
		}
	}
	
	public Vector<Arrow> Chart(String filename) {
		Vector<Arrow> arrows=new Vector<Arrow>();
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject data =  (JSONObject)((JSONObject) parser.parse(new FileReader(filename))).get("song");
	        speed=(double)data.get("speed");
	        speed/=speed_dampening;
	        JSONArray notes=(JSONArray)data.get("notes");
	        JSONArray songSectionLengths=(JSONArray)data.get("sectionLengths");
	        long songSections=(long)data.get("sections");
	        Iterator<JSONObject> it=notes.iterator();
	        while(it.hasNext()) {
	        	JSONObject section=it.next();
	        	JSONArray sectionNotes=(JSONArray) section.get("sectionNotes");
	        	boolean mH=(boolean)section.get("mustHitSection");
	        	 SectionArrows  jba=getSectionNotes(section, mH);
	        	arrows.addAll(jba.MergeComplete());
	        }
	    } catch (ParseException e) {
	    	System.out.print(e.getMessage());
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrows;
	}
	public  SectionArrows getSectionNotes(JSONObject section, boolean mustHitSection) {
		JSONArray rawNotes=(JSONArray)section.get("sectionNotes");
		boolean bothPlayers=false;
		
		Iterator<JSONArray> it2=rawNotes.iterator();
    	while(it2.hasNext()) {
    		JSONArray note=it2.next();
    		long column=(long)note.get(1);
    		if(column>3) {
    			bothPlayers=true;
    			break;
    		}
    	}

        Vector<OrphanArrow> player = new Vector<OrphanArrow>(); // BF
        Vector<OrphanArrow> opponent = new Vector<OrphanArrow>();

        if (bothPlayers) {
            opponent = _filterNotesFirstHalf(rawNotes);
            player = _filterNotesSecondHalf(rawNotes);

            if (mustHitSection) {
            	Vector<OrphanArrow> swap = player;
                player = opponent;
                opponent = swap;
            }
        } else if (mustHitSection) {
            player = _filterNotesFirstHalf(rawNotes);
        } else {
            opponent = _filterNotesFirstHalf(rawNotes);
        }

        return new SectionArrows( player, opponent );
    }
	public Vector<OrphanArrow> _filterNotesFirstHalf(JSONArray rawNotes) {
		Iterator<JSONArray> it2=rawNotes.iterator();
		Vector<JSONArray> filtered=new Vector<JSONArray>();
    	while(it2.hasNext()) {
    		JSONArray note=it2.next();
    		long column=(long)note.get(1);
    		if(column<4) {
    			filtered.add(note);
    		}
    	}
    	Vector<OrphanArrow> orphanNotes=new Vector<OrphanArrow>();
    	for(JSONArray a: filtered) {
    		double position=((Number)a.get(0)).doubleValue();
    		double duration=((Number)a.get(2)).doubleValue();
    		orphanNotes.add(new OrphanArrow(position, (long)a.get(1), duration, speed));
    	}
    	return orphanNotes;
	}
	public Vector<OrphanArrow> _filterNotesSecondHalf(JSONArray rawNotes) {
		Iterator<JSONArray> it2=rawNotes.iterator();
		Vector<JSONArray> filtered=new Vector<JSONArray>();
		while(it2.hasNext()) {
			JSONArray note=it2.next();
			long column=(long)note.get(1);
			if(column>=4) {
				filtered.add(note);
			}
		}
		Vector<OrphanArrow> orphanNotes=new Vector<OrphanArrow>();
		for(JSONArray a: filtered) {
			double position=((Number)a.get(0)).doubleValue();
    		double duration=((Number)a.get(2)).doubleValue();
			orphanNotes.add(new OrphanArrow(position,(long)a.get(1)-4,duration, speed));
		}
		return orphanNotes;
	}
}
