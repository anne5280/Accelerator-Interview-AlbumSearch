# Accelerator-Interview-AlbumSearch
Cognizant Accelerator Interview Application: This application is an album search application, which includes both a client and server.

See Repository for the following:

    Screenshot png files.
    WebArchitecture.pdf
    Readme.pdf

Dashboard Browser Application: 

Decided to just go with what I know (JS, and bootstrap) rather than trying a framework.

Class Structure
  ContextListener.java   Context class and it's main responsibility is to make sure the album catalog is loaded.
  index.jsp  User dashboard.
  Catalog.java   HttpRequest/Response servlet.
  CatalogManager.java  POJ class to encapsulate the management of the album data.  This class manages the initial load of 
                       the catalog and requests to display a range.
                       This class could be spun off into it's own service.
                       
Credits
Free images at http://clipart-library.com/record-cliparts.html

Completed All Requirements:

Given the attached .csv file of music albums, create an API with the following features:
• CRUD operation endpoints for albums
• An index of artists
• An endpoint for a particular artist that includes a listing of the artist's album(s).
• Extra Credit (required for Senior Positions), Create an endpoint that returns genres ranked by number of albums
or list the years with the most albums.
