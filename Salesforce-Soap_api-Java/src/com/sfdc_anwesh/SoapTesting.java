package com.sfdc_anwesh;

/*
 * author:anwesh,

 * Description:Salesforce soap integration with Java,
 * Operation:Crud Functionality with custom Objects Like Position__c and Candidates__c
 */
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Position__c;
import com.sforce.soap.enterprise.sobject.Candidates__c;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SoapTesting {
  
static final String USERNAME = "@@@@@@@"; //your Salesforce account name
static final String PASSWORD = "@@@@@@@";//Password securitytoken
  static EnterpriseConnection connection;

  public static void main(String[] args) {

    ConnectorConfig config = new ConnectorConfig();
    config.setUsername(USERNAME);
    config.setPassword(PASSWORD);
    //config.setTraceMessage(true);
    
    try {
      
      connection = Connector.newConnection(config);
      
      // display some current settings
      System.out.println("Auth EndPoint: "+config.getAuthEndpoint());
      System.out.println("Service EndPoint: "+config.getServiceEndpoint());
      System.out.println("Username: "+config.getUsername());
      System.out.println("SessionId: "+config.getSessionId());
      
       //Crud Operation
      queryPoistion();
      createCandiates();
      updateCandidates();
      deleteCandidates();
      
      
    } catch (ConnectionException e1) {
        e1.printStackTrace();
    }  

  }
  
  // queries and displays the 5  Positions
  private static void queryPoistion() {
    
    System.out.println("Querying for the 5 Positions...");
    
    try {
       
      // query for the 5 Positions     
      QueryResult queryResults = connection.query("SELECT Id, Position_Title__c, Educational_Requirements__c " +
      		"FROM Position__c  LIMIT 5");
      if (queryResults.getSize() > 0) {
        for (int i=0;i<queryResults.getRecords().length;i++) {
          // cast the SObject to a strongly-typed Position
        	Position__c p = (Position__c)queryResults.getRecords()[i];
 System.out.println("Id: " + p.getId() + " - Position: "+p.getPosition_Title__c()+"-->"+"_Education Req:"+" "+
              p.getEducational_Requirements__c());
        }
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }    
    
  }
  
  // create 5 test Candidates
  private static void createCandiates() {
    
    System.out.println("Creating 5 new test Candidates...");
    Candidates__c[] records = new Candidates__c[5];
    
    try {
       
      // create 5 test Candidates
      for (int i=0;i<5;i++) {
    	  Candidates__c c = new Candidates__c();
    	  c.setLast_Name__c("Testing "+i);
        c.setFirst_Name__c("Candidates "+i);
        records[i] = c;
      }
      
      // create the records in Salesforce.com
      SaveResult[] saveResults = connection.create(records);
      
      // check the returned results for any errors
      for (int i=0; i< saveResults.length; i++) {
        if (saveResults[i].isSuccess()) {
          System.out.println(i+". Successfully created record - Id: " + saveResults[i].getId());
        } else {
          Error[] errors = saveResults[i].getErrors();
          for (int j=0; j< errors.length; j++) {
            System.out.println("ERROR creating record: " + errors[j].getMessage());
          }
        }    
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }    
    
  }
  
  // updates the 5 newly created Candidates
  private static void updateCandidates() {
    
    System.out.println("Update the 5 new test Candidates...");
    Candidates__c[] records = new Candidates__c[5];
    
    try {
       
      QueryResult queryResults = connection.query("SELECT Id,Last_Name__c, First_Name__c FROM Candidates__c ORDER BY " +
      		"CreatedDate DESC LIMIT 5");
      if (queryResults.getSize() > 0) {
        for (int i=0;i<queryResults.getRecords().length;i++) {
          // cast the SObject to a strongly-typed Candidates
        	Candidates__c c = (Candidates__c)queryResults.getRecords()[i];
          System.out.println("Updating Id: " + c.getId() + " - Name: "+c.getFirst_Name__c());
          // modify the name of the Candidates
          c.setLast_Name__c(c.getLast_Name__c()+"Developer");
         // c.setFirst_Name__c(c.getFirst_Name__c()+" -- UPDATED");
          records[i] = c;
        }
      }
      
      // update the records in Salesforce.com
      SaveResult[] saveResults = connection.update(records);
      
      // check the returned results for any errors
      for (int i=0; i< saveResults.length; i++) {
        if (saveResults[i].isSuccess()) {
          System.out.println(i+". Successfully updated record - Id: " + saveResults[i].getId());
        } else {
          Error[] errors = saveResults[i].getErrors();
          for (int j=0; j< errors.length; j++) {
            System.out.println("ERROR updating record: " + errors[j].getMessage());
          }
        }    
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }    
    
  }
  
  // delete the 5 newly created Candidates
  private static void deleteCandidates() {
    
    System.out.println("Deleting the 5 new test Candidates...");
    String[] ids = new String[5];
    
    try {
       
      QueryResult queryResults = connection.query("SELECT Id, First_Name__c FROM Candidates__c ORDER BY " +
      		"CreatedDate DESC LIMIT 5");
      if (queryResults.getSize() > 0) {
        for (int i=0;i<queryResults.getRecords().length;i++) {
          // cast the SObject to a strongly-typed Candidate
        	Candidates__c a = (Candidates__c)queryResults.getRecords()[i];
          // add the Candidates Id to the array to be deleted
          ids[i] = a.getId();
          System.out.println("Deleting Id: " + a.getId() + " - Name: "+a.getFirst_Name__c());
        }
      }
      
      // delete the records in Salesforce.com by passing an array of Ids
      DeleteResult[] deleteResults = connection.delete(ids);
      
      // check the results for any errors
      for (int i=0; i< deleteResults.length; i++) {
        if (deleteResults[i].isSuccess()) {
          System.out.println(i+". Successfully deleted record - Id: " + deleteResults[i].getId());
        } else {
          Error[] errors = deleteResults[i].getErrors();
          for (int j=0; j< errors.length; j++) {
            System.out.println("ERROR deleting record: " + errors[j].getMessage());
          }
        }    
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }    
    
  }
 
}


