package com.siasun.gitdebugassist;

import android.media.MediaExtractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class MainActivity extends AppCompatActivity {

    private  int m_bTest_Git;

    public static final String sHost = "198.8.133.222";
    public static final int nPort = 21;
    public static final String sName = "test";              //匿名登录时,sName=anonymous
    public static final String sPwd = "test";

    private FTPClient m_tFtpClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_bTest_Git = 0;
    }
    private boolean  CreateConnection()
    {
        m_tFtpClient = new FTPClient();
        if (m_tFtpClient == null)
        {
            return  false;
        }
        try
        {
            m_tFtpClient.setPassive(true);
            m_tFtpClient.connect(sHost,nPort);
            m_tFtpClient.login(sName,sPwd);
        }
        catch (Exception e)
        {
            CloseConnection(m_tFtpClient);
            e.printStackTrace();
            return  false;
        }
        return  true;
    }
    private boolean CloseConnection(FTPClient tFtpClient)
    {
        if (tFtpClient != null)
        {
            if (tFtpClient.isConnected())
            {
                try
                {
                    tFtpClient.disconnect(true);
                }
                catch (Exception e)
                {
                    try
                    {
                        tFtpClient.disconnect(false);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        return  false;
                    }
                }
            }
        }
        return  true;
    }

    /***
     * 创建目录
     * @param sName
     */
    private void CreatDirectory(String sName)
    {
        try
        {
            if (m_tFtpClient == null)
            {
                return;
            }
            String sCurrentPath = m_tFtpClient.currentDirectory();
            System.out.println("currentpath = " + sCurrentPath);
            m_tFtpClient.createDirectory(sName);
        }
        catch(IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (FTPIllegalReplyException e)
        {
            e.printStackTrace();
        }
        catch (FTPException e)
        {
            e.printStackTrace();
        }
    }
    private boolean UploadFile(String sLocalPath,String sDesPath)
    {
        if (m_tFtpClient == null)
        {
            return  false;
        }
        try
        {
            if (!m_tFtpClient.currentDirectory().equals(sDesPath))
            {
                m_tFtpClient.changeDirectory(sDesPath);
            }
            File file = new File(sLocalPath);
            System.out.println("upload start");
            m_tFtpClient.upload(file);
            System.out.println("upload end");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
        return  true;
    }
    private boolean DownloadFile(String sDesPath,File tLocalFile)
    {
        if (m_tFtpClient == null)
        {
            return  false;
        }
        try
        {
            if (tLocalFile.exists() && tLocalFile.isFile())
            {
                m_tFtpClient.download(sDesPath,tLocalFile,tLocalFile.length(),null);
            }
            else
            {
                m_tFtpClient.download(sDesPath,tLocalFile);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    private String[] GetCurrentDirList()
    {
        if (m_tFtpClient == null)
        {
            return null;
        }
        try
        {
            String[] sListArray = m_tFtpClient.listNames();
            return  sListArray;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  null;
        }
    }
}
