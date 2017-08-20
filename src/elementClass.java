

import java.util.ArrayList;
import java.util.List;

public class elementClass {
	
	private String visitControlType; 	//public, private, protected,
	private String name;				//
	private String nameFatherClass;		//keyword "extend"
	private List<String> implementTable;//keyword "implements"
	
	private List<elementMethod> methodTable;
	private List<elementAttribute> attributeTable;
	
	public elementClass(){
		implementTable = new ArrayList(); 	
		methodTable = new ArrayList<elementMethod>(); 				//label j.c.
		attributeTable = new ArrayList<elementAttribute>();			//label j.c.		
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
		System.out.println("------------------"+visitControlType);
    }
	public void setName (String name) {
        this.name = name;
    }
	public void setNameFatherClass(String name) {
        this.nameFatherClass = name;
        //getNameFatherClass();
       
    }
	
	public void setImplementTable(List<String> implementTable) {
        this.implementTable = implementTable;
        //getImplementTable();
    }
	public void setMethodTable(List<elementMethod> methodTable) {
        this.methodTable = methodTable;
        //getMethodTable();
    }
	public void setAttributeTable(List<elementAttribute> attributeTable) {
        this.attributeTable = attributeTable;
        //getAttributeTable();
    }
	
	public String getName() {
		return name;       
    }	
	public String getNameFatherClass() {
		System.out.println("extends:------------------"+this.nameFatherClass);  
		return nameFatherClass;
    }
	
	public List<String> getImplementTable() {
		//for(String str : implementTable)
		//	System.out.println("CLASS INS implementTable:------------------"+str);
		return implementTable;
    }
	public List<elementMethod> getMethodTable() {
		//for(String str : implementTable)
		//	System.out.println("implementTable:------------------"+str); 
		//System.out.println("CLASS INS MethodTable Size:------------------"+this.methodTable.size());
		return methodTable;
    }
	public List<elementAttribute> getAttributeTable() {
		//System.out.println("CLASS INS AttributeTable Size:------------------"+this.attributeTable.size());
		return attributeTable;
    }

}
