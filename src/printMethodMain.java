

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.github.javaparser.ast.internal.Utils.ensureNotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class printMethodMain {
	
	/*
	 // UML Class Diagram Generator based on JavaParser and yUML
	  * by Jiongfeng Chen, SE
	  * input file : java source files
	  * 03/30/2016
	  * tested with 5 test cases
    */
	
	public static printMethodMain outer = new printMethodMain();
	public static MyPrint myprint = outer.new MyPrint();
	
	
	public static void main(String[] args) throws Exception { 
				
		//read single java file mode
		/*
		 // creates an input stream for the file to be parsed
		 FileInputStream in = new FileInputStream("../uml-parser-test-1/A.java");
		 CompilationUnit cu;
	     try {
	           // parse the file
	           cu = JavaParser.parse(in);
	     } finally {
	          in.close();
	     }  
	     */
		
		//read multiple java file mode
		//read the designated folder(test cases01-05)'s all *.java files, 
		
		if(args.length == 2)
		{
			String testcasePath = args[0];
			String imageName = args[1];
		
		//static String testcasePath="../uml-parser-test-1";
			
		List<String> filePath =new ArrayList();
	    
	    	 
	    File dirFiles[] = new File(testcasePath).listFiles();	    	
		for(File temp : dirFiles ){
			if(!temp.isFile()){
				//findFile(temp); 	//not subfolder this case
			}
			//find the .java file
			if(temp.isFile() && temp.getAbsolutePath().endsWith(".java") ){
				System.out.println(temp.isFile() + "  " + temp.getAbsolutePath() + ":" + temp.getName());
				filePath.add(temp.getAbsolutePath());
				System.out.println("!!!Number of java files!!!"+filePath.size());
			}
		}	         
		
	    List<elementClass> classTable;
		List<elementInterface> interfaceTable;
		classTable= new ArrayList<elementClass>();
		interfaceTable= new ArrayList<elementInterface>();
		CompilationUnit cu;
	    for(String strfilePath : filePath)
	    {
	    	FileInputStream in = new FileInputStream(strfilePath);	    
		    try {
		    	// parse the file
		        cu = JavaParser.parse(in);
		        System.out.println(cu.toString());	 
		        
		  	    //compileUnitList.add(readJavaCompileUnit(testcasePath + "/" +  temp.getName()));
		        myprint.classInstance = new elementClass(); 				//label j.c.
		        myprint.interfaceInstance = new elementInterface(); 		//label j.c.
		        myprint.methodTable = new ArrayList<elementMethod>(); 		//label j.c.
		        myprint.attributeTable = new ArrayList<elementAttribute>(); //label j.c.

		  	    myprint.methodTable.clear();	     
		  		outer.new ClassInterfaceVisitor().visit(cu, null);		 
		  		myprint.attributeTable.clear();
		  		new MethodVisitor().visit(cu, null);		  		
		  		new ConstructorVisitor().visit(cu, null);
		  		if(myprint.isVisitingClass){
		  			myprint.classInstance.setMethodTable(myprint.methodTable);
	        	}else{
	        		myprint.interfaceInstance.setMethodTable(myprint.methodTable);
	        	}		  		
		  		new FieldVisitor().visit(cu, null);
		  		myprint.classInstance.setAttributeTable(myprint.attributeTable);
		  		if(myprint.isVisitingClass){
		  			classTable.add(myprint.classInstance);
	        	}else{
	        		interfaceTable.add(myprint.interfaceInstance);
	        	}
		  		
		  		
		    } finally {
		         in.close();
		    } 
	    }
	    
	    //analyze class relationships from myprint
	    //generate class and interface namelist
	    List<String> strClassNameList = new ArrayList();
	    for (int i=0; i<classTable.size(); i++)
	    	strClassNameList.add(classTable.get(i).getName());//
	    List<String> strInterfaceNameList = new ArrayList();
	    for (int i=0; i<interfaceTable.size(); i++)
	    	strInterfaceNameList.add(interfaceTable.get(i).getName());//
	    
	    System.out.println("---Class NameList---" + strClassNameList);
	    //generate class nameFatherClass
	    for (int i=0; i<classTable.size(); i++){
	    	String str = classTable.get(i).getNameFatherClass();	    	 
	    	if(str ==null || str.isEmpty()){
	    		System.out.println("extends:x-novalue-null");
	    	}
	    	else{
	    		System.out.println(str);
	    	}
	    }
	    //generate class ImplementsTable
	    for (int i=0; i<classTable.size(); i++){
	    	List<String> strList = classTable.get(i).getImplementTable();	    		    	
		    if(strList ==null || strList.isEmpty()){
		    	System.out.println("implement: xstrList-novalue-null");
		    }
		    else{
		    	for(int j=0; j<strList.size(); j++)
		    		System.out.println(strList.get(j));
		    }		    
	    }
	    //generate class MethodTable
	    for (int i=0; i<classTable.size(); i++){
	    	List<elementMethod> methodList = classTable.get(i).getMethodTable();	    		    	
		    if(methodList ==null || methodList.isEmpty()){
		    	System.out.println("methods: xList-novalue-null");
		    }
		    else{
		    	for(int j=0; j<methodList.size(); j++){
		    		System.out.println("begin method---"+Integer.toString(i)+methodList.get(j).getVisitControlType());
		    		System.out.println(methodList.get(j).getType());
		    		System.out.println(methodList.get(j).getName());
		    		System.out.println(methodList.get(j).getParaTypeTable());
		    		System.out.println(methodList.get(j).getParaNameTable()+"---end method");
		    	}
		    }		    
	    }
	    //generate class attributeTable
	    for (int i=0; i<classTable.size(); i++){
	    	List<elementAttribute> attributeList = classTable.get(i).getAttributeTable();	    		    	
		    if(attributeList ==null || attributeList.isEmpty()){
		    	System.out.println("attributes: xList-novalue-null");
		    }
		    else{
		    	for(int j=0; j<attributeList.size(); j++){
		    		System.out.println("begin attribute---"+Integer.toString(i)+attributeList.get(j).getVisitControlType());
		    		System.out.println(attributeList.get(j).getType());
		    		System.out.println(attributeList.get(j).getName()+"---end method");
		    	}
		    }		    
	    }
	    //generate relationship table,7-tuple relationship table
	    //[0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel; iorc, 1-class, 0-interface	
	    String sevenTuple[ ][ ] = new String[500][ ];				//create
	    int counter_Tuple = 0;
	    for(int i=0; i<500; i++){
	    	sevenTuple[i] = new String[7];
	    }
	    if(strClassNameList.size()!=0){
	    	for(int i=0; i<strClassNameList.size(); i++){ //i<1,strClassNameList.size();
	    		//find the 1st class	    		
	    		//create associate class or interface lists
	    		List<String> relNameList= new ArrayList();
	    		List<String> relTypeList= new ArrayList();
	    		//find its extends
	    		String strExtend = classTable.get(i).getNameFatherClass();	    	 
	    	    if(strExtend ==null || strExtend.isEmpty()){
	    	    	System.out.println("extends:x-novalue-null");
	    	    }else{
	    	    	relNameList.add(strExtend);
	    	    	relTypeList.add("AgeneralizeB");	    	    	
	    	    }	    	    
	    		//find its implements
	    	    List<String> strImpleList = classTable.get(i).getImplementTable();	    		    	
			    if(strImpleList ==null || strImpleList.isEmpty()){
			    	System.out.println("implement: xstrList-novalue-null");
			    }
			    else{
			    	for(int j=0; j<strImpleList.size(); j++){
			    		relNameList.add(strImpleList.get(j));
			    		relTypeList.add("ArealizeB");
			    	}
			    }
	    		//find its attributes types list
			    List<elementAttribute> attrList = classTable.get(i).getAttributeTable();	    		    	
			    if(attrList ==null || attrList.isEmpty()){
			    	System.out.println("attributes: xList-novalue-null");
			    }
			    else{
			    	for(int j=0; j<attrList.size(); j++){
			    		for(int k=0; k<strClassNameList.size(); k++){//isClass
			    			if(attrList.get(j).getType().equals(strClassNameList.get(k))){
			    				relNameList.add(attrList.get(j).getType());
					    		relTypeList.add("AassociateB"); //B is attribute of A
			    			}
			    		}
			    		if(attrList.get(j).getType().contains("[]")){//isClass[]
			    			//String str ="We are students";
			    			//int size = str.indexOf("a"); //return 3
			    			//1 String str = "Hello word"; //substring(int beginIndex,  int endIndex)
			    			//2 String substr = str.substring(0,3); //substr, "hel"
			    			//fetch type before "[]" 
			    			String strAttrType_t = attrList.get(j).getType();
			    			int begIndex_t = strAttrType_t.indexOf("[");
			    			strAttrType_t = strAttrType_t.substring(0, begIndex_t).trim();
			    			for(int k=0; k<strClassNameList.size(); k++){
			    				if(strAttrType_t.equals(strClassNameList.get(k))){
				    				relNameList.add(strAttrType_t);
						    		relTypeList.add("Aassociate*[]B"); //B is attribute*[] of A
				    			}
			    			}
			    		}
			    		if(attrList.get(j).getType().contains("<") && attrList.get(j).getType().contains(">")){
			    			//fetch type between "<" and ">" 
			    			String strAttrType_t = attrList.get(j).getType();
			    			int begIndex_t = strAttrType_t.indexOf("<");
			    			int endIndex_t = strAttrType_t.indexOf(">");			    			
			    			strAttrType_t = strAttrType_t.substring(begIndex_t+1, endIndex_t).trim();			    			
			    			for(int k=0; k<strClassNameList.size(); k++){
			    				if(strAttrType_t.equals(strClassNameList.get(k))){
				    				relNameList.add(strAttrType_t);
						    		relTypeList.add("Aassociate*<>B"); //B is attribute*<> of A
				    			}
			    			}
			    			//add ??? for strInterfaceNameList
			    		}
			    	}
			    }
	    		//find its methods parameter list
			    List<elementMethod> methList = classTable.get(i).getMethodTable();	    		    	
			    if(methList ==null || methList.isEmpty()){
			    	System.out.println("methods: xList-novalue-null");
			    }
			    else{
			    	for(int j=0; j<methList.size(); j++){
			    		for(int k=0; k<methList.get(j).getParaTypeTable().size(); k++){
			    			for(int p=0; p<strClassNameList.size(); p++)
				    			if(methList.get(j).getParaTypeTable().get(k).equals(strClassNameList.get(p))){
				    				relNameList.add(methList.get(j).getParaTypeTable().get(k));
						    		relTypeList.add("AusesB"); //B is parameter for the method of A
				    			}
			    			for(int p=0; p<strInterfaceNameList.size(); p++)
				    			if(methList.get(j).getParaTypeTable().get(k).equals(strInterfaceNameList.get(p))){
				    				relNameList.add(methList.get(j).getParaTypeTable().get(k));
						    		relTypeList.add("AusesB"); //B is parameter for the method of A
				    			}
			    			if(methList.get(j).getParaTypeTable().get(k).contains("[]")){//isClass[]
			    				//fetch type before "[]" 
					    		String strParaType_t = methList.get(j).getParaTypeTable().get(k);
					    		int begIndex_t = strParaType_t.indexOf("[");
					    		strParaType_t = strParaType_t.substring(0, begIndex_t).trim();
					    		for(int m=0; m<strClassNameList.size(); m++){
					    			if(strParaType_t.equals(strClassNameList.get(m))){
						    			relNameList.add(strParaType_t);
								    	relTypeList.add("Auses*[]B"); //B is parameter *[] of A
						    		}
					    		}
					    		for(int m=0; m<strInterfaceNameList.size(); m++){
					    			if(strParaType_t.equals(strInterfaceNameList.get(m))){
						    			relNameList.add(strParaType_t);
								    	relTypeList.add("Auses*[]B"); //B is parameter *[] of A
						    		}
					    		}
					    	}
					    	if(methList.get(j).getParaTypeTable().get(k).contains("<") && methList.get(j).getType().contains(">")){
					    		//fetch type between "<" and ">" 
					    		String strParaType_t = methList.get(j).getParaTypeTable().get(k);
					    		int begIndex_t = strParaType_t.indexOf("<");
					    		int endIndex_t = strParaType_t.indexOf(">");
					    		strParaType_t = strParaType_t.substring(begIndex_t+1, endIndex_t).trim();
					    		for(int m=0; m<strClassNameList.size(); m++){
					    			if(strParaType_t.equals(strClassNameList.get(m))){
						    			relNameList.add(strParaType_t);
								    	relTypeList.add("Auses*<>B"); //B is parameter *<> of A
						    		}
					    		}
					    		for(int m=0; m<strInterfaceNameList.size(); m++){
					    			if(strParaType_t.equals(strInterfaceNameList.get(m))){
						    			relNameList.add(strParaType_t);
								    	relTypeList.add("Auses*<>B"); //B is parameter *<> of A
						    		}
					    		}					    		
					    	}
			    		}
			    	}			    	
			    }//end "find methods parameter list" //relationship table for one class generated
			    
			    
			    //print out relNameList, relTypelist of one class
			    System.out.println("relNameList," + relNameList);
			    System.out.println("relTypeList," + relTypeList);
			    // remove A-A self relation && !attrList.get(j).getType().equals(classTable.get(i).getName())
			    for(int j=0; j<relNameList.size(); j++)
			    	if(relNameList.get(j).equals(strClassNameList.get(i))){
			    		relNameList.remove(j);
			    		relTypeList.remove(j);
			    	}			    	
			    
			    //save to 7-tuple relationship table 
			    //String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel; 
			    //iorc_, 1-class, 0-interface
			    for(int j=0; j<relNameList.size(); j++){
			    	sevenTuple[counter_Tuple][0]=Integer.toString(i);
			    	sevenTuple[counter_Tuple][2]=Integer.toString(1);
			    	for(int k=0; k<strClassNameList.size(); k++){
			    		if(relNameList.get(j).equals(strClassNameList.get(k))){
			    			sevenTuple[counter_Tuple][1]=Integer.toString(k);
			    			sevenTuple[counter_Tuple][3]=Integer.toString(1);
			    		}			    			
			    	}
			    	for(int k=0; k<strInterfaceNameList.size(); k++){
			    		if(relNameList.get(j).equals(strInterfaceNameList.get(k))){
			    			sevenTuple[counter_Tuple][1]=Integer.toString(k);
			    			sevenTuple[counter_Tuple][3]=Integer.toString(0);
			    		}			    			
			    	}
			    	sevenTuple[counter_Tuple][4]=strClassNameList.get(i);
	    			sevenTuple[counter_Tuple][5]=relNameList.get(j);
			    	sevenTuple[counter_Tuple][6]=relTypeList.get(j);
			    	
			    	counter_Tuple =counter_Tuple+1;			    
			    } 		    
			   
			    
	    	}//class loop finish (loop i)
	    	
	    	//print the 7-tuple relationship table
	    	for(int i=0;i<counter_Tuple;i++){  
	            for(int j=0;j<7;j++){	                  
	                System.out.print(sevenTuple[i][j]+", ");  
	            }  
	            System.out.print("rel-" + Integer.toString(i)+"\n");  
	    	}
	    	
	    }//classNamelit not empty
	    
	    //generate yUML Class Diagram script
	    /* Auses*<>B		--- "[A]uses 1-.-*>[B]" 		
	     * Auses*[]B		--- "[A]uses 1-.-*>[B]"
	     * AusesB			--- "[A]uses -.->[B]"
	     * Aassociate*<>B	--- "[A]1-0..*[B]" OR "[A]-*[B]"
	     * Aassociate*[]B	--- "[A]1-0..*[B]" OR "[A]-*[B]"
	     * AassociateB		---	"[A]-[B]"
	     * AgeneralizeB		--- "[B]^-[A]"
	     * ArealizeB		--- "[<<B>>]^-.-[A]"	    
    	*/
	    //class details yUML script
	    //i.e. [User|+Forename;Surname;+HashedPassword;-Salt|+Login();+Logout()] 
	    String compactStrRelateScript = " "; //label , j.c. Final output script(yUML compact mode)
	    for (int i=0; i<classTable.size(); i++){
	    	String strClassScript;	    	
	    	strClassScript = "[" +classTable.get(i).getName();	    	
	    	List<elementAttribute> attributeList = classTable.get(i).getAttributeTable();	    		    	
		    if(attributeList ==null || attributeList.isEmpty()){		    	
		    }
		    else{
		    	boolean isAttrPrint = false;
		    	for(int j=0; j<attributeList.size(); j++){
		    		boolean isClassInterf = false;
		    		if(attributeList.get(j).getType().contains("<") && attributeList.get(j).getType().contains(">") ){
	    				//fetch type between "<" and ">" 
				    	String strAttrType_CDel_t = attributeList.get(j).getType();
				    	int begIndex_t = strAttrType_CDel_t.indexOf("<");
				    	int endIndex_t = strAttrType_CDel_t.indexOf(">");
				    	strAttrType_CDel_t = strAttrType_CDel_t.substring(begIndex_t+1, endIndex_t).trim();
				    	for(int m=0; m<strClassNameList.size(); m++){
				    		if(strAttrType_CDel_t.equals(strClassNameList.get(m))){
				    			isClassInterf=true;
					    	}
				    	}
				    	for(int m=0; m<strInterfaceNameList.size(); m++){
				    		if(strAttrType_CDel_t.equals(strInterfaceNameList.get(m))){
				    			isClassInterf=true;
					    	}
				    	}
	    			}else{
	    				for (int k=0; k<strClassNameList.size(); k++){		    					    			
			    			if(strClassNameList.get(k).equals(attributeList.get(j).getType())){
			    				isClassInterf=true;
			    			}
			    		}
			    		for (int k=0; k<strInterfaceNameList.size(); k++){
			    			if(strInterfaceNameList.get(k).equals(attributeList.get(j).getType())){
			    				isClassInterf=true;
			    			}
			    		}
	    			}		    		
		    		if(!isClassInterf){		    			
			    		if(attributeList.get(j).getVisitControlType() !=null && !attributeList.get(j).getVisitControlType().isEmpty()){
			    			if(!isAttrPrint)
			    				strClassScript = strClassScript + "|";
			    			if(attributeList.get(j).getVisitControlType().contains("public"))
				    			strClassScript = strClassScript + "+ ";
				    		if(attributeList.get(j).getVisitControlType().contains("private"))
				    			strClassScript = strClassScript + "- ";
				    		//if(attributeList.get(j).getVisitControlType().contains("protected"))
				    			//strClassScript = strClassScript + "# ";
				    		isAttrPrint=true;				    		
			    		
			    		    if(!attributeList.get(j).getVisitControlType().contains("protected")){
					    		strClassScript = strClassScript + attributeList.get(j).getName() +" : ";
					    		if(attributeList.get(j).getType().contains("[]")){
					    			strClassScript = strClassScript + attributeList.get(j).getType().replace("[]", "(*)") +";";
					    		}
					    		else{
					    			strClassScript = strClassScript + attributeList.get(j).getType() +";";
					    		}
			    		    }
			    		}
		    		}
		    	}
		    	
		    }
	    	List<elementMethod> methodList = classTable.get(i).getMethodTable();	    		    	
		    if(methodList ==null || methodList.isEmpty()){
		    }
		    else{
		    	strClassScript = strClassScript + "|";
		    	for(int j=0; j<methodList.size(); j++){
		    		if(!methodList.get(j).getName().contains("get") && !methodList.get(j).getName().contains("set")){		    			
		    			if(methodList.get(j).getVisitControlType() !=null && !methodList.get(j).getVisitControlType().isEmpty()){
			    			if(methodList.get(j).getVisitControlType().contains("public"))
				    			strClassScript = strClassScript + "+ ";
				    		if(methodList.get(j).getVisitControlType().contains("private"))
				    			strClassScript = strClassScript + "- ";
				    		if(methodList.get(j).getVisitControlType().contains("protected"))
				    			strClassScript = strClassScript + "# ";
			    		}			    		
			    		strClassScript = strClassScript + methodList.get(j).getName() +"(";
			    		for(int k=0; k<methodList.get(j).getParaNameTable().size(); k++){
			    			strClassScript = strClassScript + methodList.get(j).getParaNameTable().get(k);
			    			strClassScript = strClassScript + " : " +  methodList.get(j).getParaTypeTable().get(k);
			    			if(k!=(methodList.get(j).getParaNameTable().size()-1))
			    					strClassScript = strClassScript + " , ";
			    		}
			    		strClassScript = strClassScript + ")";
			    		if(methodList.get(j).getType()==null || methodList.get(j).getType().isEmpty())
			    			strClassScript = strClassScript +  ";";
			    		else
			    			strClassScript = strClassScript +  " : " + methodList.get(j).getType() + ";";
		    		}//method is getter or setter, ignore		    		
		    	}//script for method list
		    }//script - method list not empty
		    strClassScript = strClassScript + "],";
		    System.out.println(strClassScript); //label j.c.
		    if(i==0){
		    	compactStrRelateScript = strClassScript;
		    }else{
		    	compactStrRelateScript = compactStrRelateScript +strClassScript;
		    }
	    }//script - loop for class(attr, method)    
	    
	    //relationship yUML script
	    /* [<<interface>>;A]
	     * program will merge (Aassociate*<>B,Aassociate*[]B,AassociateB)
	     * Auses*<>B		--- "[A]uses 1-.-*>[B]" 		
	     * Auses*[]B		--- "[A]uses 1-.-*>[B]"
	     * AusesB			--- "[A]uses -.->[B]"
	     * Aassociate*<>B	--- "[A]1-0..*[B]" OR "[A]-*[B]"
	     * Aassociate*[]B	--- "[A]1-0..*[B]" OR "[A]-*[B]"
	     * AassociateB		---	"[A]-[B]"
	     * AgeneralizeB		--- "[B]^-[A]"
	     * ArealizeB		--- "[<<B>>]^-.-[A]"	    
    	*/
	    for(int i=0;i<counter_Tuple;i++){
	    	String strRelateScript = " ";
	    	for(int j=0;j<counter_Tuple;j++){
	    		if((i!=j) && sevenTuple[i][4].equals(sevenTuple[j][5]) && 
	    				sevenTuple[i][5].equals(sevenTuple[j][4]) && 
	    				sevenTuple[i][6].contains("associate") &&
	    				sevenTuple[j][6].contains("associate")){//merge
	    			if(!sevenTuple[i][6].equals(sevenTuple[j][6])){
	    				if(sevenTuple[i][6].length()>=sevenTuple[j][6].length())
		    				sevenTuple[j][6]=sevenTuple[i][6];
		    			else
		    				sevenTuple[i][6]=sevenTuple[j][6];
	    			}   			
	    		}
	    	}
	    	if(i==0){
	    		String rel_t;
	    		if(sevenTuple[i][6].equals("Auses*[]B") || sevenTuple[i][6].equals("Auses*<>B")){
	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    			rel_t = "uses 1-.-*>";
	    			if(sevenTuple[i][2].equals("0")){
	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    			}else{
	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    			}
	    				if(sevenTuple[i][3].equals("0")){
		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
		    			}else{
		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
		    			}	    				
	    			
	    		}	    		
	    		if(sevenTuple[i][6].equals("AusesB")){
	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    			rel_t = "uses -.->";
	    			if(sevenTuple[i][2].equals("0")){
	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    			}else{
	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    			}
	    				if(sevenTuple[i][3].equals("0")){
		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
		    			}else{
		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
		    			}	    				
	    			
	    		}
	    		if(sevenTuple[i][6].equals("Aassociate*<>B") || sevenTuple[i][6].equals("Aassociate*[]B")){
	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    			rel_t = "1-0..*";
	    			if(sevenTuple[i][2].equals("0")){
	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    			}else{
	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    			}
	    				if(sevenTuple[i][3].equals("0")){
		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
		    			}else{
		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
		    			}	    				
	    			
	    		}
	    		if(sevenTuple[i][6].equals("AassociateB")){
	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    			rel_t = "-";
	    			if(sevenTuple[i][2].equals("0")){
	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    			}else{
	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    			}
	    				if(sevenTuple[i][3].equals("0")){
		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
		    			}else{
		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
		    			}	    				
	    			
	    		}
	    		if(sevenTuple[i][6].equals("AgeneralizeB")){
	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    			rel_t = "^-";
	    			if(sevenTuple[i][3].equals("0")){
	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][5] + "]" + rel_t;	    				
	    			}else{
	    				strRelateScript = "[" + sevenTuple[i][5] + "]" + rel_t;
	    			}
	    				if(sevenTuple[i][2].equals("0")){
		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][4] + "],";	    				
		    			}else{
		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][4] + "],";
		    			}	    				
	    			
	    		}	    		
	    		if(sevenTuple[i][6].equals("ArealizeB")){
	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    			rel_t = "^-.-";
	    			if(sevenTuple[i][3].equals("0")){
	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][5] + "]" + rel_t;	    				
	    			}else{
	    				strRelateScript = "[" + sevenTuple[i][5] + "]" + rel_t;
	    			}
	    				if(sevenTuple[i][2].equals("0")){
		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][4] + "],";	    				
		    			}else{
		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][4] + "],";
		    			}	    			
	    		}
	    		System.out.println(strRelateScript); //label j.c.
	    		compactStrRelateScript = compactStrRelateScript + strRelateScript;
	    	}else{//start from seven-tuple[2], find out print before or not
	    		boolean printCurr=false;	    		
	    		for(int j=0;j<i;j++){
	    			boolean equalTuple=true;
	    			for(int jk=0; jk<7; jk++){//same rel as before
	    				if(!sevenTuple[i][jk].equals(sevenTuple[j][jk])){	
	    					equalTuple=false;
		    				break;
		    			}	    				
	    			}
	    			if(equalTuple){
	    				printCurr=true;
	    				break;
	    			}	    			
	    			if(sevenTuple[i][4].equals(sevenTuple[j][5]) && 
    				sevenTuple[i][5].equals(sevenTuple[j][4]) && 
    				sevenTuple[i][6].contains("associate") &&
    				sevenTuple[j][6].contains("associate")){
	    				printCurr=true;
	    				break; //current rel has been print
	    			}
	    		}
	    		if(!printCurr){
	    				String rel_t;
	    	    		if(sevenTuple[i][6].equals("Auses*[]B") || sevenTuple[i][6].equals("Auses*<>B")){
	    	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    	    			rel_t = "uses 1-.-*>";
	    	    			if(sevenTuple[i][2].equals("0")){
	    	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    	    			}else{
	    	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    	    			}
	    	    				if(sevenTuple[i][3].equals("0")){
	    		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
	    		    			}else{
	    		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
	    		    			}	    				
	    	    			
	    	    		}	    		
	    	    		if(sevenTuple[i][6].equals("AusesB")){
	    	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    	    			rel_t = "uses -.->";
	    	    			if(sevenTuple[i][2].equals("0")){
	    	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    	    			}else{
	    	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    	    			}
	    	    				if(sevenTuple[i][3].equals("0")){
	    		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
	    		    			}else{
	    		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
	    		    			}	    				
	    	    			
	    	    		}
	    	    		if(sevenTuple[i][6].equals("Aassociate*<>B") || sevenTuple[i][6].equals("Aassociate*[]B")){
	    	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    	    			rel_t = "1-0..*";
	    	    			if(sevenTuple[i][2].equals("0")){
	    	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    	    			}else{
	    	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    	    			}
	    	    				if(sevenTuple[i][3].equals("0")){
	    		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
	    		    			}else{
	    		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
	    		    			}	    				
	    	    			
	    	    		}
	    	    		if(sevenTuple[i][6].equals("AassociateB")){
	    	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    	    			rel_t = "-";
	    	    			if(sevenTuple[i][2].equals("0")){
	    	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][4] + "]" + rel_t;	    				
	    	    			}else{
	    	    				strRelateScript = "[" + sevenTuple[i][4] + "]" + rel_t;
	    	    			}
	    	    				if(sevenTuple[i][3].equals("0")){
	    		    				strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][5] + "],";	    				
	    		    			}else{
	    		    				strRelateScript = strRelateScript + "[" + sevenTuple[i][5] + "],";
	    		    			}	    				
	    	    			
	    	    		}
	    	    		if(sevenTuple[i][6].equals("AgeneralizeB")){
	    	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    	    			rel_t = "^-";
	    	    			if(sevenTuple[i][3].equals("0")){
	    	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][5] + "]" + rel_t;	    				
	    	    			}else{
	    	    				strRelateScript = "[" + sevenTuple[i][5] + "]" + rel_t;
	    	    			}
	    	    			if(sevenTuple[i][2].equals("0")){
	    		    			strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][4] + "],";	    				
	    		    		}else{
	    		    			strRelateScript = strRelateScript + "[" + sevenTuple[i][4] + "],";
	    		    		}	    				
	    	    			
	    	    		}	    		
	    	    		if(sevenTuple[i][6].equals("ArealizeB")){
	    	    			//String [0]id_A, [1]id_B, [2]iorc_A, [3]iorc_B, [4]name_A, [5]name_B, [6]rel;
	    	    			rel_t = "^-.-";	    	    			
	    	    			if(sevenTuple[i][3].equals("0")){
	    	    				strRelateScript = "[<<interface>>;" + sevenTuple[i][5] + "]" + rel_t;	    				
	    	    			}else{
	    	    				strRelateScript = "[" + sevenTuple[i][5] + "]" + rel_t;
	    	    			}
	    	    			if(sevenTuple[i][2].equals("0")){
	    		    			strRelateScript = strRelateScript + "[<<interface>>;" + sevenTuple[i][4] + "],";	
	    		    			
	    		    		}else{
	    		    			strRelateScript = strRelateScript + "[" + sevenTuple[i][4] + "],";	    		    			    		    				
	    		    		}
	    	    		}
	    	    		System.out.println(strRelateScript); //label j.c.
	    	    		compactStrRelateScript = compactStrRelateScript + strRelateScript;
	    			}//find out print or not: not case
	    		
	    	}//find out print or not (from seven-touple [2])
	    	
                           
    	}//strRelateScript loop finish
	    
	    
	    //test
	    for(int i=0; i<classTable.size();i++){
	    	if(classTable.get(i).getName().equals("ConcreteDecoratorB")){
	    		//System.out.println(classTable.get(i).getMethodTable().size());
	    		//System.out.println(classTable.get(i).getAttributeTable().get(0).getName()); 
	    	}	    	
	    }
	    
	    compactStrRelateScript.substring(0, compactStrRelateScript.length()-2); 
	    System.out.println("compact output: " + compactStrRelateScript);
	    outer.yUMLtoImage("http://yuml.me/diagram/scruffy/class/"+compactStrRelateScript.toString(), imageName);
		}
	    else{
	    	System.out.println("Please Provide 2 input. Example: \"javaParserUMLClassDiagram ..\\testcase1 classDiagram_case1.jpg\" ");
	    }
	    	
	    	
	    
	 }// main()	
	
	//convert yUML script(output) to image
	public static void yUMLtoImage(String URL, String imageName)
	{
		try
		{
			URL url = new URL(URL.toString());
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int i = 0;
			while (-1!=(i=in.read(buf)))
			{
			  out.write(buf, 0, i);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			FileOutputStream fileOut = new FileOutputStream(imageName);
			fileOut.write(response);
			fileOut.close();		
		}
		catch(Exception e)
		{
			System.out.println("Please add file extension to image file name.");
			e.printStackTrace();
		}
	}
		
		
	 private class MyPrint{
		 
		 public elementClass classInstance;
		 public elementInterface interfaceInstance;
		 public List<elementMethod> methodTable;
		 public List<elementAttribute> attributeTable;
		 boolean isVisitingClass;
		 
		 public MyPrint(){			 
			 classInstance = new elementClass(); //label j.c.
			 interfaceInstance = new elementInterface();
			 methodTable = new ArrayList<elementMethod>();
			 attributeTable = new ArrayList<elementAttribute>();			 
		 }
		 
		 public void getMethodTable() {
			 System.out.println("!--methodTable Size:------------------"+methodTable.size());			       
		 }
		 public void getAttributeTable() {
			 System.out.println("!--AttributeTable Size:------------------"+attributeTable.size());			       
		 }
	 }

	 private class ClassInterfaceVisitor extends VoidVisitorAdapter {
		  
		 String nameFatherClass;
		 List<String> implementTable;
		 
		 public ClassInterfaceVisitor(){
			 implementTable= new ArrayList();			 			 
		 }
					
	        @Override
	        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
	            //list all the attributes of in this CompilationUnit
	        	//find out interface or class
	        	if(n.isInterface()){
	        		myprint.isVisitingClass = false;
	        	}else{
	        		myprint.isVisitingClass = true;
	        	}
	        	if(n.isInterface()){
	        		myprint.interfaceInstance.setVisitControlType(n.getModifiers());
	        		myprint.interfaceInstance.setName(n.getName());
	        		
	        	}else{
	        		int i;
		        	i=n.getModifiers();
		        	System.out.println(i);
		        	myprint.classInstance.setVisitControlType(n.getModifiers());
		        	myprint.classInstance.setName(n.getName());
	        	}
	        	System.out.println(n.isInterface()); 	        	
	        	System.out.println(n.getModifiers()+ " :modifier end;");	//label j.c.
	        	System.out.println(n.getName() + " :name end;"); 	  
	        	
	        		        	
	        	List<ClassOrInterfaceType> extendsTable = n.getExtends();
	        	for (ClassOrInterfaceType var : extendsTable){
	        		if(!n.isInterface()){//only one father class situation
	        			myprint.classInstance.setNameFatherClass(var.getName());	        		
		        	}
	    			System.out.println(var);
	    		}  		
	        	List<ClassOrInterfaceType> implementsTable = n.getImplements();
	        	implementTable.clear();
	        	int i=0;
	        	for (ClassOrInterfaceType var : implementsTable){
	        		if(!n.isInterface()){	        			
	        			implementTable.add(var.getName());     		
		        	}	        		
	    			System.out.println(var);
	    			System.out.println("implementable:---"+implementTable.get(i++));
	    		}  	
	        	if(!n.isInterface()){
	        		myprint.classInstance.setImplementTable(implementTable);    		
	        	}   
	        	//List<TypeParameter> typeParameterTable = n.getTypeParameters();
	        	//for (TypeParameter var : typeParameterTable){
	    		//	System.out.println(var);
	    		//} 
	        	//variables = ensureNotNull(variables);
	            super.visit(n, arg);
	        }
	    }
	 
	    private static class MethodVisitor extends VoidVisitorAdapter {

	    	elementMethod methodInstance;	    	
	    	List<String> paraTypeTable;
	    	List<String> paraNameTable;      		
      		
	    	public MethodVisitor(){
	    		methodInstance = new elementMethod();
	    		paraTypeTable= new ArrayList();	
	    		paraNameTable= new ArrayList();
			 }
	    	      		 
	        @Override
	        public void visit(MethodDeclaration n, Object arg) {
	            // list all the methods of in this CompilationUnit, including inner class methods
	        	
	        	methodInstance = new elementMethod(); //label j.c.
	        	paraTypeTable= new ArrayList();	
	        	paraNameTable= new ArrayList();
	        	methodInstance.setVisitControlType(n.getModifiers());
	        	methodInstance.setName(n.getName());
	        	methodInstance.setType(n.getType().toString());
        		
        		System.out.println(n.getModifiers() + " :modifier end;");
	        	System.out.println(n.getType() + " :type end;"); 
	        	System.out.println(n.getName() + " :name end;");
	        	
	        	List<Parameter> parameterTable = new ArrayList<Parameter>();
	        	parameterTable = n.getParameters();
	        	paraTypeTable.clear();
		    	paraNameTable.clear(); 
		    	int i=0;
	        	for (Parameter var : parameterTable){// access the method's parameters
	        		paraTypeTable.add(var.getType().toString());
	        		paraNameTable.add(var.getId().toString());
	        		i=i+1;
	        		System.out.println("PARAMETERS_" +Integer.toString(i) );
	    			System.out.println(var);
	    			System.out.println(var.getType());	//parameter's type
	    			System.out.println(var.getId()); 	//parameter's name
	    		} 
	        	methodInstance.setParaTypeTable(paraTypeTable);
	        	methodInstance.setParaNameTable(paraNameTable); 
	        	//System.out.println("before.......");
	        	System.out.println("before......."+methodInstance.getName());
	        	System.out.println("before.size......"+myprint.methodTable.size());
	        	for(int jkk=0;jkk<myprint.methodTable.size();jkk++){
	        		System.out.println("before.......");
	        		//System.out.println(myprint.methodTable.get(jkk).getName());
	        	}
	        	myprint.methodTable.add(methodInstance); //label j.c.
	        	System.out.println("got method table.......");
	        	myprint.getMethodTable();
	            super.visit(n, arg);
	        }
	    }
	    
	    private static class ConstructorVisitor extends VoidVisitorAdapter {
	    	//label j.c.

	    	elementMethod methodInstance;	    	
	    	List<String> paraTypeTable;
	    	List<String> paraNameTable;      		
      		
	    	public ConstructorVisitor(){
	    		methodInstance = new elementMethod();
	    		paraTypeTable= new ArrayList();	
	    		paraNameTable= new ArrayList();
			 }	    	      		 
	    	 
	        @Override
	        public void visit(ConstructorDeclaration n, Object arg) {
	            // list the constructor of in this CompilationUnit, including inner class methods
	        	
	        	methodInstance = new elementMethod(); //label j.c.
	        	paraTypeTable= new ArrayList();	
	        	paraNameTable= new ArrayList();
	        	methodInstance.setVisitControlType(n.getModifiers());
	        	methodInstance.setName(n.getName());
	        	//methodInstance.setType(n.getType().toString());
        		
        		System.out.println(n.getModifiers() + " :modifier end;");
	        	//System.out.println(n.getType() + " :type end;"); 
	        	System.out.println(n.getName() + " :name end;");
	        	
	        	List<Parameter> parameterTable = new ArrayList<Parameter>();
	        	parameterTable = n.getParameters();
	        	paraTypeTable.clear();
		    	paraNameTable.clear(); 
		    	int i=0;
	        	for (Parameter var : parameterTable){// access the method's parameters
	        		paraTypeTable.add(var.getType().toString());
	        		paraNameTable.add(var.getId().toString());
	        		i=i+1;
	        		System.out.println("PARAMETERS_" +Integer.toString(i) );
	    			System.out.println(var);
	    			System.out.println(var.getType());	//parameter's type
	    			System.out.println(var.getId()); 	//parameter's name
	    		} 
	        	methodInstance.setParaTypeTable(paraTypeTable);
	        	methodInstance.setParaNameTable(paraNameTable); 
	        	//System.out.println("before.......");
	        	System.out.println("before......."+methodInstance.getName());
	        	System.out.println("before.size......"+myprint.methodTable.size());
	        	for(int jkk=0;jkk<myprint.methodTable.size();jkk++){
	        		System.out.println("before.......");
	        		//System.out.println(myprint.methodTable.get(jkk).getName());
	        	}
	        	myprint.methodTable.add(methodInstance); //label j.c.
	        	System.out.println("got method table.......");
	        	myprint.getMethodTable();
	            super.visit(n, arg);
	        }
	    }
	    
	    private static class FieldVisitor extends VoidVisitorAdapter {
	    	
	    	elementAttribute attributeInstance;	    	
		    
	    	public FieldVisitor(){
	    		attributeInstance = new elementAttribute();	    		
			 }
	    	
	        @Override
	        public void visit(FieldDeclaration n, Object arg) {
	            // list all the attributes of in this CompilationUnit
	        	attributeInstance = new elementAttribute();
	        	System.out.println(n.getType() + " :type end;//modifier"); 
	        	System.out.println(n.getModifiers());
	        	attributeInstance.setVisitControlType(n.getModifiers());
	        	attributeInstance.setType(n.getType().toString());	        	 
	        	
	        	List<VariableDeclarator> variableTable = n.getVariables();	        	
	    		for (VariableDeclarator var : variableTable){	    			
	    			attributeInstance.setName(var.toString());
	    			//System.out.println(var);
	    		}  		
	        	//System.out.println(n.getName() + " :name end;"); 
	    		myprint.attributeTable.add(attributeInstance); //label j.c.
	    		myprint.getAttributeTable();
	        		            
	            super.visit(n, arg);
	        }
	    }

}
