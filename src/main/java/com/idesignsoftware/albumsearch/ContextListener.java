package com.idesignsoftware.albumsearch;


import java.io.BufferedReader;
import java.io.File;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.idesignsoftware.albumsearch.*;

@WebListener("application context listener")
public class ContextListener implements ServletContextListener {
	
	public static final String ALBUM_CONTEXT = "ALBUM_CONTEXT";
	
	private CatalogManager manager = new CatalogManager();

		
	@Override
	public void contextInitialized(ServletContextEvent event) {
				
		System.out.println("Initializing context...loading album list.");
        ServletContext context = event.getServletContext();
        
        // Make init results available to front end
        event.getServletContext().setAttribute(ALBUM_CONTEXT, manager);
        
        System.out.println("...context Initialized.");

	}
	
	public static ContextListener getInstance(ServletContext context) {
		return (ContextListener) context.getAttribute(ALBUM_CONTEXT);
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Context destroyed called...");
		
	}
}
