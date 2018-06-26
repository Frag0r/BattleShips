import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

class Vokabelspiel implements ActionListener
{
private Wörter[] wortListe;
private int difficulty;
private int wortWahl;
public int gameround=0;
private JDialog fenster;
private JLabel anzeigetext;
public boolean Go,Running;
public static String filename;
private Timer timer;
private long startTime = -1;
private long duration = 3000;
private File sprachDatei;
public final int ulx,uly;

public Vokabelspiel(int x, int y){
    Document doc = null;
    String standard = "lang1.xml";
    
    if(Vokabelspiel.filename == null) {
    	sprachDatei = new File(standard);
    }
    else {
    	sprachDatei = new File(Vokabelspiel.filename);
    }
    ulx = x;
    uly = y;
    try {
        SAXBuilder builder = new SAXBuilder();
        doc = builder.build(sprachDatei);
        Element root = doc.getRootElement();
        int levelCount  = root.getChildren().size();
        wortListe = new Wörter[levelCount];
        
        for(int i=0;i<levelCount;i++) {
        	String name = "Level"+(i+1);
        	List<Element> LISTE = root.getChild(name).getChildren();
        	wortListe[i] = new Wörter(LISTE);
        }     
    } catch (JDOMException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
} 
public static void menu() {
	File ordner=new File(".");
    File[]dateiliste=ordner.listFiles(new TextFileFilter());
    
    JList displayList = new JList(dateiliste);
    JScrollPane listeFenster = new JScrollPane(displayList);
    JFrame f = new JFrame("Sprachfiles");
    
     displayList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList)evt.getSource();
            if (evt.getClickCount() == 2) {
                int index = list.locationToIndex(evt.getPoint());
                switch(JOptionPane.showConfirmDialog(listeFenster, "Wollen Sie die Datei "+dateiliste[index]+" wirklich laden ?")) {
                case 0:
                	Vokabelspiel.filename = dateiliste[index].getName();
                	f.dispose();
                	break;
                case 1:
                	break;
                case 2:
                	f.dispose();
                	break;
                }
            } 
        }
    });
	 displayList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
     displayList.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
     displayList.setName("displayList");
     displayList.setVisibleRowCount(-1);
     f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     f.setSize(250, 200);
     f.add(listeFenster);
     f.setVisible(true);
}

public int play() {
	if(Go) {
		setDifficulty(0);
		fenster = new JDialog();
		fenster.setTitle("COUNTDOWN");
		fenster.setSize(200,200);
		fenster.setModal(true);
		fenster.setLocation(ulx, uly);
		anzeigetext = new JLabel();
		anzeigetext.setText("Timer");
		timer = new Timer(10, this);
		timer.setInitialDelay(0);
		startTimer();
		fenster.add(anzeigetext);
		fenster.pack();
		fenster.setVisible(true);  
		Go = false;
		return 0;
    }
	else {
	String antwort;
	timer = new Timer(10, this);
	timer.setInitialDelay(0);
    startTimer();
    fenster.setModal(false);
    fenster.setVisible(true); 
	do {
		antwort = JOptionPane.showInputDialog(fenster, getRandomWort(), "");
	}while(!this.getResult(antwort) && timer.isRunning());
		if(antwort == null) {
			return 3;
		}
		else {
			timer.stop();
			return 2;
		}
	}
}
private String getRandomWort() {
	int random = (int) (wortListe[difficulty].getWortanzahl()*Math.random());
	setWortwahl(random);
	return wortListe[difficulty].getWort(random);
}
	   
private boolean getResult(String antwort) {
	return wortListe[getDifficulty()].Verify(antwort, getWortwahl());
}
private void setDifficulty(int diff) {
	difficulty = diff;
}
public int getDifficulty() {
	return this.difficulty;
}
private void setWortwahl(int wort) {
	wortWahl = wort;
}
public int getWortwahl() {
	return this.wortWahl;
}
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	if(e.getSource() == timer) {
		
		long now = System.currentTimeMillis();
	    long clockTime = now - startTime;
		SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
		anzeigetext.setText(df.format(duration - clockTime));
		if((clockTime*2 == duration)) {
			setDifficulty(1);
		}
		  if (clockTime >= duration) {
		        clockTime = duration;
		        timer.stop();
		        duration = 30000;
		        startTime = -1;
		        fenster.setVisible(false);
		    }
	}
}
public void startTimer() {
	if (startTime < 0) {
        startTime = System.currentTimeMillis();
    }
    long now = System.currentTimeMillis();
    long clockTime = now - startTime;
  
    SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
    anzeigetext.setText(df.format(duration - clockTime));
    
    timer.addActionListener(this);
	if (!timer.isRunning()) {
        timer.start();
    }
}
public boolean isRunning() {
	return Running;
}

}
class TextFileFilter implements FileFilter {
    public boolean accept(File file) {
        String name=file.getName();
        return name.length()<28&&name.endsWith(".xml");
        }
    }