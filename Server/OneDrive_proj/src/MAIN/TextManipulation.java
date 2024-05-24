package MAIN;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Formatter;

public class TextManipulation {
    private Scanner input;
    private Formatter output;

   public void openFileRead(String FileName)
   {  try
      {  input = new Scanner( new File( FileName) );
      }  // end try
      catch ( FileNotFoundException fileNotFoundException )
      {  System.err.println( "Error opening file." );
         System.exit( 1 );
      }  // end catch
   }  // end method openFile

   
   public String readRecords()  // read record from file
   {  // object to be written to screen
      String sMsgClara = "";
      try   // read records from file using Scanner object
      {  while( input.hasNext() )
         {  
            sMsgClara += input.next();
         }  // end while
      }  // end try
      catch( NoSuchElementException elementException )
      {  System.err.println( "File improperly formed." );
         input.close();
         System.exit( 1 );
      }  // end catch
      catch( IllegalStateException stateException )
      {  System.err.println( "Error reading from file." );
         System.exit( 1 );
      }  // end catch
      return sMsgClara;
   }  // end method readRecords

public void closeFileRead() // close file and terminate application
{  if( input != null )
         input.close(); // close file
   }
   public void openFile(String FileName)  // enable user to open file
    {  try
        {  output = new Formatter(FileName);
        }  // end try
        catch( SecurityException securityException )
        {  System.err.println( "You do not have write access to this file." );
            System.exit( 1 );
        }  // end catch
        catch( FileNotFoundException filesNotFoundException )
        {  System.err.println( "Error creating file." );
            System.exit( 1 );
        } // end catch
    } // end method openFile

   public void addRecords(String mensagem)   // add records to file
   {  // object to be written to file
        try // output values to file
         {  // retrieve data to be output
               output.format( "%s\n", mensagem);
         }  // end try
         catch ( FormatterClosedException formatterClosedException )
         {  System.err.println( "Error writing to file." );
            return;
         } // end catch // end catch
         catch (NoSuchElementException elementException) {
             System.err.println("Invalid input. Please try again.");
             input.nextLine(); // discard input so user can try again
         } // end catch
        // end while
   }  // end method addRecords

   public void closeFile() // close file
   {  if ( output != null )
         output.close();
   }
}
