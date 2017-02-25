
####################################
#CS/COE 1501 Project 2 (Simplified)#
####################################

 [X] 1. Download example files
 [X] 2. Make MyLZW
 [X] 3. Read/understand provided code
 [ ] 4. Changes to LZW in MyLZW
		
    [X] - Algorithm will vary size of out -> in codewords from 9 -> 16 bits
		
    [X] - Codeword size should increase when all previous ones are used up
			- Constants W and L should be changed to be non-final variables for variable-length encoding (obviously)
		
		- Three Options upon Filled Codebook:

	    [X] a. Do Nothing: continue using full codebook (already done in LZW.java)

		[X] b. Reset: reset dict back to initial state for new codewords. 
				- Be careful ab where you reset, bc compression and expansion might fall outta sync (will take time!)
				- Helpful to trace areas when codeword size increases & dict is reset
				- One idea is to have an extra output file for each of the compress() and expand() methods to output any trace code. 

					Printing out (codeword, string) pairs in the iterations just before and after a bit change or reset is done can help you a lot to synchronize your code properly.
			
		[ ] c. Monitor: Begin monitoring the compression ratio whenever filling the codebook
				- Begin recording compression ratios whenever the codebook is filled and do nothing until then
				- Compression ratio defined as the size of the uncompressed data that has 	been read in so far divided by the size of the compressed data processed so far
				- If the ratio degrades beyond a certain threshold from when the last codeword was added, then reset the dict back to its initial state (much like in reset mode). 
					The	threshold is defined as a ratio of compression ratios
				- Old ratio = compression ratio when program last filled the codebook
				- New ratio = current compression ratio post-fill
			
			% Be especially careful with the dictionary reset and monitor compression ratio options. These are very tricky and take alot of 
				thought to get to work. Think about what happens when the dictionary is reset and what is necessary to do in the compress() 
				and expand() methods. I recommend getting the variable width codeword part of the program to work first and then moving on to 
				implementing Reset mode and Monitor mode.
			
			% The mode will be chosen by the program and recorded at the beginning of the output filled pre-compression so that it can be automatically 
				retrieved upon expansion. Compression/expansion should use the same mode.
			
			% To set the mode, the program should accept 3 new command line arguments  
				
				"n" for Do Nothing mode
				"r" for Reset mode
				"m" for Monitor mode

			% LZW already provides command line processing, and i/o files provide their own i/o redirection (&lt for input file, &gt for output file), 
				so new arguments n,r,m should be handled in much the same way:

					Compression:	java MyLZW - r < foo.txt > foo.lzw     
					  Expansion:	java MyLZW + < foo.lzw > foo2.txt

			 	 The above example does not overwrite foo.txt, so use this method for testing!

 [ ] 5. Evaluate MyLZW's performance on the 14 provided example files and organize the results in "results.txt"
 		
 		- Original file size, compressed file size, and compression ratio (original/compressed)
 		
 			- Unmodified LZW program		
 			- MyLZW in Do Nothing Mode
 			- 	" 	in Reset Mode
  			- 	" 	in Monitor Mode 		
 			- Another existing compression application of my choice (7zip, WinZIP, etc.)

 [ ] 6. Fill out info_sheet.txt