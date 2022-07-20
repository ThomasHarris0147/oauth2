package io.muzoo.scalable.nettube.videoclient.upload.compress;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Helps Java Scripts connect with Python Scripts
 * Only works with this application -> very specific python script
 */
public class PythonJavaHelper {
    /**
     * run sendTask.py python script
     * CAVEAT: must run all workers first
     * @param videoLocation location of the video
     */
    public static void sendTaskToWorkers(String videoLocation){
        Process process;
        Process mProcess;
        String scriptsLocation = "src\\main\\java\\io\\muzoo\\scalable\\nettube\\backend\\upload\\compress\\";
        try{
            process = Runtime.getRuntime().exec(new String[]{"python",
                    new File(".").getCanonicalPath()+"/backend/"+scriptsLocation+"sendTask.py",
                    videoLocation});
            mProcess = process;
        }catch(Exception e) {
            System.out.println("Exception Raised" + e.toString());
            return;
        }
        InputStream stdout = mProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        String line;
        try{
            while((line = reader.readLine()) != null){
                System.out.println("stdout: "+ line);
            }
        }catch(IOException e){
            System.out.println("Exception in reading output"+ e.toString());
        }
    }

    /**
     * purge brothers. it is time to purge.
     * vanquish yourselves of sin and step into truth brother.
     * Coding and Technology was invented by the devil.
     */
    public static void purgeQueues() {
        Process process;
        Process mProcess;
        String scriptsLocation = "src\\main\\java\\io\\muzoo\\scalable\\nettube\\backend\\upload\\compress\\";
        try{
            process = Runtime.getRuntime().exec(new String[]{"python",
                    new File(".").getCanonicalPath()+"/Backend/"+scriptsLocation+"purqeQueues.py"});
            mProcess = process;
        }catch(Exception e) {
            System.out.println("Exception Raised" + e.toString());
            return;
        }
        InputStream stdout = mProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        String line;
        try{
            while((line = reader.readLine()) != null){
                System.out.println("stdout: "+ line);
            }
        }catch(IOException e){
            System.out.println("Exception in reading output"+ e.toString());
        }
    }
}
