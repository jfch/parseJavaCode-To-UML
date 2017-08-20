

public class elementAttribute {
	private String visitControlType; 	//public, private, protected,
	private String type;				//int, string, ...
	private String name;				//

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
	public String getVisitControlType () {
        //System.out.println("attr visitControlType---"+visitControlType);
        return visitControlType;
    }
	public String getName () {
		//System.out.println("attr name---"+name);
		 return name;
    }
	public String getType () {
		//System.out.println("attr type---"+type);
		 return type;
	}
	
}
