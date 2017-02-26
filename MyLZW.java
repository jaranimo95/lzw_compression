public class MyLZW {

   // Globals	
	private static final double comprThreshold = 1.1;
    private static final int R = 256;        // number of input chars
    private static 		 int L = 512;        // number of codewords = 2^W
    private static 		 int W = 9;          // codeword width

    public static void compress(char mode) {

    	BinaryStdOut.write(mode);					  // Write mode character to file for use in expand()
    	String input = BinaryStdIn.readString();	  // reads in filename to compress
        TST<Integer> st = new TST<Integer>();			 
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);				  // Populates codebook with keys all 8-bit extended ASCII values (0-255)
        int code = R+1;  // R is codeword for EOF

        String prefix = "";
        int t = 0;

        boolean monitorFlag = false;	// Monitor Mode: OFF (will be turned on later if monitor mode selected)

        long readIn = 0;		// Data (in bits) that's been read in from the file
        long compressed = 0;	// Data (in bits) that's been read in and compressed
        double ratioOld = 1;	// Old compression ratio
        double ratioCurr = 0;	// Current compression ratio

        while ( input.length() > 0 ) {

         	prefix = st.longestPrefixOf(input);							// Find max prefix match in input
         	BinaryStdOut.write(st.get(prefix), W);      				// Print s's encoding.
            t = prefix.length();										// Get length of prefix
            
            readIn += ( t*16 );											// Add length of prefix times 16 (bits) to uncompressed data counter
            compressed += W;											// Add bitsize of codeword to compressed data counter
            
            ratioCurr = ( (double) readIn / (double) compressed );		// Calculate compression ratio

            if ( t < input.length() && code >= L && W < 16 )			// If codebook is full (max # of codewords) and width is less than 16 (RESIZE)
         		L = (int) Math.pow(2,++W);								// Increase codeword width and max amount of codewords         	

           // If maximum codewords / codeword width reached
			if ( code >= L && W == 16 ) {        	   
	         	if ( !monitorFlag ) {	// If Monitor Mode: OFF
	         	   // Reset Mode Selected: Reset codebook with initial contents (0->255 (0->R-1) used)
		         	if (mode == 'r'){	
		         		st = new TST<Integer>();
		        		for (int i = 0; i < R; i++)
		            		st.put("" + (char) i, i);
		         		W = 9;								// Set codeword width back to initial state of 9 bits
		         		L = 512;							// Set max number of codewords to 2^W = 2^9 = 512
		         		code = R+1;							// Set code back to R+1
	         		}
	         	   // Monitor Mode Selected: Save current compression ratio to begin monitoring ratio of compression ratios
	         		else if ( mode == 'm' ) {
	         			ratioOld = ratioCurr;
	         			monitorFlag = true;	// Monitor Mode: ON
	         		}	
	         	} else { // Else Monitor Mode: ON	
	         		if ( ( ratioOld / ratioCurr ) >= comprThreshold ) {
            			st = new TST<Integer>();		// Reset codebook
		        		for (int i = 0; i < R; i++)
		            		st.put("" + (char) i, i);
		         		W = 9;							// Set codeword width back to initial state of 9 bits
		         		L = 512;						// Set max number of codewords to 2^W = 2^9 = 512
		         		code = R+1;						// Set code back to R+1
		         		monitorFlag = false;			// Monitor Mode: OFF					
            		}
	         	}
         	}

            if ( t < input.length() && code < L )					// If prefix is shorter than input and still space for more codewords
                st.put(input.substring(0, t + 1), code++);			// Add prefix to symbol table.
        
         	input = input.substring(t);            // Scan past s in input.        
        }
        BinaryStdOut.write(R,W);
        BinaryStdOut.close();
    } 

    public static void expand() {
        
    	char mode = BinaryStdIn.readChar();		// Read in mode character from beginning of file ( written at line 34 of compress() )

        long readIn = 0;		// Data that's been read in from the file
        long expanded = 0;	// Data that's been compressed
        double ratioOld = 1;	// Old compression ratio
        double ratioCurr = 0;	// Current compression ratio
        boolean monitorFlag = false;	// Monitor Mode: OFF (will be turned on later if monitor mode selected)

        String[] st = new String[(int) Math.pow(2,16)];	// Should be large enough to handle max # of codewords
        int code;									// next available codeword value

        // initialize symbol table with all 1-character strings
        for (code = 0; code < R; code++)
            st[code] = "" + (char) code;
        st[code++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);	// Read in int of length W
        if (codeword == R) return;           	// expanded message is empty string
        readIn += W;							// Add character to amount to amount of data read in
        
        String val = st[codeword];				
        int t = val.length();
        expanded += ( t*16 );

        while (true) {

        	ratioCurr = (double) expanded / (double) readIn;

        	if (code >= L && W < 16) 			// If codebook is full (max # of codewords) and width is less than 16 (RESIZE)
        		L = (int) Math.pow(2,++W);		// Increase codeword width and max amount of codewords

        	// If maximum codewords / codeword width reached
			if ( code >= L && W == 16 ) {        	   
	         	if ( !monitorFlag ) {	// If Monitor Mode: OFF
	         	   // Reset Mode Selected: Reset codebook with initial contents (0->255 (0->R-1) used)
		         	if (mode == 'r'){	
		         		st = new String[(int) Math.pow(2,16)];			// Reset codebook
		        		for (int i = 0; i < R; i++)
		            		st[i] = "" + (char) i;
		         		W = 9;							// Set codeword width back to initial state of 9 bits
		         		L = 512;						// Set max number of codewords to 2^W = 2^9 = 512
		         		code = R+1;						// Set code back to R+1
		         		monitorFlag = false;			// Monitor Mode: OFF
	         		}
	         	   // Monitor Mode Selected: Save current compression ratio to begin monitoring ratio of compression ratios
	         		else if ( mode == 'm' ) {
	         			ratioOld = ratioCurr;
	         			monitorFlag = true;	// Monitor Mode: ON
	         		}	
	         	} else { // Else Monitor Mode: ON	
	         		if ( ( ratioOld / ratioCurr ) >= comprThreshold ) {
            			st = new String[(int) Math.pow(2,16)];			// Reset codebook
		        		for (int i = 0; i < R; i++)
		            		st[i] = "" + (char) i;
		         		W = 9;							// Set codeword width back to initial state of 9 bits
		         		L = 512;						// Set max number of codewords to 2^W = 2^9 = 512
		         		code = R+1;						// Set code back to R+1
		         		monitorFlag = false;			// Monitor Mode: OFF					
            		}
	         	}
         	}

            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (code == codeword) s = val + val.charAt(0);   // special case hack
            if (code < L) st[code++] = val + s.charAt(0);
            val = s;
            t = val.length();
            readIn += W;
            expanded += ( t*16 );
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        	 if (args[0].equals("-")) {
        	 	char mode = args[1].charAt(0);
        	 	String validOptions = "nrm";
        	 	if(validOptions.indexOf(mode) < 0)
        	 		System.out.println("\nInvalid Mode - Valid choices are as follows:\n\tDo Nothing: 'n'\n\t   Monitor: 'm'\n\t     Reset: 'r'");
        	 	else 
        	 		compress(mode);
        	 }
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}