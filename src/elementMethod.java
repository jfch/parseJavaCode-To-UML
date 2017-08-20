

import java.util.ArrayList;
import java.util.List;

public class elementMethod {
	private String visitControlType; 	//public, private, protected,
	private String type;				//int, string, ...
	private String name;				//
	private List<String> paraTypeTable;
	private List<String> paraNameTable;
	
	public elementMethod(){
		paraTypeTable = new ArrayList(); 				//label j.c.
		paraNameTable = new ArrayList();			//label j.c.		
	}
	
	public void setVisitControlType (int i) {
		//conversion table, default:0;  public:1; private:2;protected:4; public static:9 		
		if(i==0)
			this.visitControlType = "default";
		if(i==1)
			this.visitControlType = "public";
		if(i==2)
			this.visitControlType = "private";
		if(i==4)
			this.visitControlType = "protected";
		if(i==9)
			this.visitControlType = "public static";
		//getVisitControlType();
    }
	public void setType (String type) {
        this.type = type;
        //getType();
    }
	public void setName (String name) {
        this.name = name;
        //getName();
    }
	public void setParaTypeTable(List<String> paraTypeTable) {
        this.paraTypeTable = paraTypeTable;
        //getParaTypeTable();
    }
	public void setParaNameTable(List<String> paraNameTable) {
        this.paraNameTable = paraNameTable;
        //getParaNameTable();
    }
	
	public String getVisitControlType () {
        //System.out.println("method visitControlType---"+visitControlType);
        return visitControlType;
    }
	public String getName () {
		//System.out.println("method name---"+name);
		return name;
    }
	public String getType () {
		 //System.out.println("method type---"+type);
		 return type;
	}	
	public List<String> getParaTypeTable() {
		//for(String str : paraTypeTable)
			//System.out.println("paraTypeTable:------------------"+str); 
		return paraTypeTable;
    }
	public List<String> getParaNameTable() {
		//for(String str : paraNameTable)
			//System.out.println("paraNameTable:------------------"+str);   
		return paraNameTable;
    }
	

}
