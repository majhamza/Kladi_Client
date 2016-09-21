package com.kaldi.reco.client;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;



public class ClientReco {


public static void main(String[] args) throws SocketException, IOException {
	//long startTime = System.nanoTime();
	//exemple d'exécution
//	File f=new File("/home/majhamza/Bureau/teste/teste2/wav/Abdellah/file.txt");
//	Scanner s=new Scanner(f);
//	while(s.hasNextLine()){
//		
//		String t=s.nextLine();
//		exec("10.10.1.208","/home/majhamza/Bureau/teste/teste2/wav/Abdellah/"+t,"/home/majhamza/reco/log/log2.txt");
//	}
//	exec("10.10.1.208","/home/majhamza/Bureau/teste/teste/nv/conv/safi_s2.WAV","/home/majhamza/reco/log/log2.txt");	
	//long stopTime = System.nanoTime();
	//double f= (stopTime-startTime)/1000000;
	//System.out.println(f);
	
	
	//args[0] est le chemin vers le fichier audio
	//exec("10.10.1.21",args[0],"");
	exec("192.168.43.36","/home/majhamza/Téléchargements/Reco_locuteurs/Ismael/bruits/ismael_002_M_21/117c_cafe0.wav","");
		  }



    //ip: l'adresse ip de la vm
	//file_path: le chemin complet vers le fichier audio qu'on veut transmettre à la vm
	//log_path: le chemin du dossier où on veut garder le log des reconnaissances
public static void exec(String ip, String file_path, String log_path) throws SocketException, IOException{
	String s=connect(ip,file_path);
	//long startTime = System.nanoTime();
	String r=reco(ip,s);
	//long stopTime = System.nanoTime();
	//double f= (stopTime-startTime)/1000000;
	//System.out.println(f);
	Scanner sc=new Scanner(r);
	// les reconnaisseurs retournent plusieurs résultats, le résultat du meilleur reconnaisseur
	// est affiché dans la console et on garde trace du résultat principale et les autres résultats
	// dans le log.
		    
		    String st=r.substring(r.indexOf(' ')+1,r.indexOf('|')-1);
		   System.out.println(st);
		   // String st2=r.substring(r.indexOf('|')+1);
			//logWriter(st2,log_path);
	
}

public static void exec2(String ip, String file_path, String log_path) throws SocketException, IOException{
	String s=connect(ip,file_path);
	String r=reco(ip,s);
	Scanner sc=new Scanner(r);
	// les reconnaisseurs retournent plusieurs résultats, le résultat du meilleur reconnaisseur
	// est affiché dans la console et on garde trace du résultat principale et les autres résultats
	// dans le log.
		    
		    String st=r.substring(0,r.indexOf('|'));
		    System.out.println(st+"\n-------------------------------------------------\n");
		    String st2=r.substring(r.indexOf('|')+1);
			logWriter(st2,log_path);
	
}


	
	public static String reco(String ip, String nom){
		//System.out.println("Reconnaissance du fichier en cours..");
		String[] a={MediaType.TEXT_PLAIN};
		Client client=ClientBuilder.newClient();
		WebTarget target=client.target("http://"+ip+":8080/com.kaldi.reco/rest"
				+ "/kaldi?fichier="+nom);
		Response resStatus=(Response) target.request(a).get(Response.class);
		String response=(String) target.request(a).get(String.class);
        return nom+": "+response;
		
	}
	
	public static String connect(String ip, String chemin) throws SocketException, IOException{

		FTPClient cl=new FTPClient();
		cl.connect(ip);
		cl.login("kaldi","azerty@123");
		cl.enterLocalPassiveMode();
		cl.setFileType(FTP.BINARY_FILE_TYPE);
		
		String s=chemin.substring(chemin.lastIndexOf('/')+1);
		
		// APPROACH #1: uploads first file using an InputStream
        File firstLocalFile = new File(chemin);

        String firstRemoteFile = "reco/"+s;
        InputStream is = new FileInputStream(firstLocalFile);
        
        //System.out.println("Transfert du fichier "+s+" en cours...");
        boolean done = cl.storeFile(firstRemoteFile, is);
        is.close();
        if (done) {
            //System.out.println("Le transfret du fichier a été effectué avec succés.\n");
        }
        
        return s;
        
        
        }
	
	// results : est le résultat de la reconnaissance
	// chemin : est le chemin du fichier log
	public static void logWriter(String results, String chemin) throws IOException{
		FileWriter f=new FileWriter(chemin, true);
	    PrintWriter w=new PrintWriter(f);
	    BufferedWriter b=new BufferedWriter(f);
	    w.println(results);
	    w.close();
		
	}
		
	}