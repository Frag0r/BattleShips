import java.util.List;

import org.jdom2.Element;

public class Wörter {

private String[][] Liste;
private int[] resultCount;
private int WortAnzahl;
	
public Wörter(List<Element> LISTE) {
	this.Liste = initializeWorterliste(LISTE);
}
private int getMinResults(List<Element> Woerterliste) {
	int puffer = 0;
	for(int i=0;i<Woerterliste.size();i++) {
	String trans = Woerterliste.get(i).getAttributeValue("translation");
  	String[]AllTrans = trans.split(",");
	  	if(AllTrans.length > puffer) {
	  		puffer = AllTrans.length;
	  	}
	}
	return puffer+1;
}

private String[][] initializeWorterliste(List<Element> Woerterliste) {
	setWortanzahl(Woerterliste.size());
	resultCount = new int[Woerterliste.size()];
	String[][] Liste = new String[getWortanzahl()][getMinResults(Woerterliste)];
	
	for(int i=0;i<getWortanzahl();i++) {
        Liste[i][0] = Woerterliste.get(i).getValue();
        String trans = Woerterliste.get(i).getAttributeValue("translation");
    	String[]AllTrans = trans.split(",");
    	resultCount[i] = AllTrans.length;
	        for(int j=1;j<resultCount[i]+1;j++) {
	        	Liste[i][j] = AllTrans[j-1];
	        }
        }
	return Liste;
}
private void setWortanzahl(int anzahl) {
	this.WortAnzahl = anzahl;
}
public int getWortanzahl() {
	return this.WortAnzahl;
}
public String getWort(int wortwahl) {
	return Liste[wortwahl][0];
}
public void setWort(int wortwahl,String wort) {
	Liste[wortwahl][0] = wort;
}
public boolean Verify(String antwort,int wortwahl) {
	boolean result = false;
	for(int i=1;i<resultCount[wortwahl]+1;i++) {
		if(Liste[wortwahl][i].equals(antwort)) {
			result = true;
			break;
		}
	}
	return result;
}
}
