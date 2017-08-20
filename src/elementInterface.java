

import java.util.ArrayList;
import java.util.List;

public class elementInterface {
	
	private String visitControlType; 	//public, private, protected,
	private String name;				//
	private List<String> classTable;	//associated class list
	
	private List<elementMethod> methodTable;
	
	public elementInterface(){
		classTable = new ArrayList(); 	
		methodTable = new ArrayList<elementMethod>(); 				//label j.c.		
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
    }
	public void setName (String name) {
        this.name = name;
    }
	public void setMethodTable(List<elementMethod> methodTable) {
        this.methodTable = methodTable;
        //getMethodTable();
    }
	
	public String getName() {
		return name;       
    }
	public String getVisitControlType() {
		return visitControlType;       
    }
	public List<elementMethod> getMethodTable() {
		//for(String str : implementTable)
		//	System.out.println("implementTable:------------------"+str); 
		System.out.println("INTERFACE INS MethodTable Size:------------------"+this.methodTable.size());
		return methodTable;
    }

}
