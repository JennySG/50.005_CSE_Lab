import java.io.*;
import java.util.*;

public class SimpleShell {
    public static void main(String[] args) throws java.io.IOException {
       String commandLine;
       BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
       ProcessBuilder pb = new ProcessBuilder();
       ArrayList<String> history = new ArrayList<String>();
       Process p;

       while(true){
        // read what the user entered
	    System.out.print("jsh>");
	    commandLine = console.readLine();

	    // if the user entered a return, just loop again
	    if (commandLine.equals("")) {
		    continue;
	    }else{// if the user entered a number for history search
           int num;
           try{
             num = Integer.parseInt(commandLine);
             if(num>=history.size()){
                System.out.println("invalid comand history search!");
             }else{
                history.add(history.get(num));
                String[] commandList = (history.get(num)).split(" ");
                pb.command(commandList);
                p = pb.start();
                printProcess(p); 
             }
           }catch(NumberFormatException ex){
             if(commandLine.equals("history")){
               if(history.size()==0){
                 System.out.println("no history yet!");
               }else{
                 System.out.println(historyContent(history));
               }
             }else if(commandLine.equals("!!")){
               String[] commandList = (history.get(history.size()-1)).split(" ");
               history.add(history.get(history.size()-1));
               pb.command(commandList);
               p = pb.start();
               printProcess(p);
             }else{ // not a history search, proceed to split the user input.
                history.add(commandLine);
                try{String[] commandList;
                    commandList = commandLine.split(" ");

                    if (commandList[0].equals("cd")){ //if the user entered cd
                       File f;
                       if (commandList.length==1){  
                          //check cd to home
                          pb.directory(new File(System.getProperty("user.home")));
                       }else if (commandList[1].equals("..")){
                          //check for parent directory
                          String parent = new File(System.getProperty("user.dir")).getParent();
                          pb.directory(new File(parent));
                          System.setProperty("user.dir", parent);
                       }else{ //check if directory valid
                          f = new File(System.getProperty("user.dir")+"/"+commandList[1]);
                          if (!f.isDirectory()){
                             System.out.println("error: new directory is invalid!"+f.getAbsolutePath());
                          }//go to new directory
                          else{
                            pb.directory(f);
                            System.setProperty("user.dir", f.getAbsolutePath());
                          }
                       }  
                       p = pb.start();                  
                    }else{
                        pb.command(commandList);       
                        p = pb.start();
                        printProcess(p);
                    }
		        }catch(Exception ex1){
			       System.out.println(ex1.getMessage());
		        }
              }
           }
        }
      }
    }

    public static void printProcess(Process p) throws java.io.IOException{
       BufferedReader buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
       String result = null;
       while ((result=buffer.readLine())!=null){
          System.out.println(result);
       }
       buffer.close();
    }

    public static String historyContent(ArrayList<String> history){
       String str="";
       for (int i=0; i<history.size(); i++){
           str+=(i+" "+history.get(i)+"\n");
       }
       return str;
    }
}

 

