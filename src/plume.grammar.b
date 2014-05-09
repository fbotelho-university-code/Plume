//F�bio Botelho 41625. The lexer, bnf, and ast definitions for the plume compiler
 
Package plume; 

Helpers 
letter = [[0x0041 .. 0x005A] + [0x0061 .. 0x007A]];  
digit = ['0'..'9'];

//Not used: 
//connectives_bin_op = '&&' | '||'; 
//arithmetic_bin_op = '+' | '-' | '/' | '*' | '%'  ;
//comparison_bin_op = '<=' | '<' | '==' | '!=' | '>' | '>=' ;      

input = [0..0xffff];
newline = 10; 
sp = ' ' ; 
tab = 9; 
escape_sequence = '\b' | '\t' | '\n' | '\f' | '\r' | '\"' | '\' ''' | '\\' ;
string_character = [input - ['"' + '\']] | escape_sequence;
nonl = [[input + newline] - newline];
nostar = [input - '*']; 
noaspas = [input - '"'];
not_star_slash = [nostar  - '/'];

Tokens

string = '"' string_character* '"';
white_space = sp | tab | newline; 

//operators :
//Not used: 
//bin_op = connectives_bin_op | comparison_bin_op | arithmetic_bin_op ;

//Arithemtic operations
plus = '+';
div = '/';
mul = '*'; 
mod = '%';
minus = '-';

//Logical operations
le =  '<='; 
l =  '<'; 
eq = '==';
neq = '!=';
b = '>'; 
be = '>='; 
and = '&&'; 
or = '||';  

//Others : 
not = '!'; 
colon = ':'; 
seta = '->'; 
comma = ','; 
l_par = '('; 
r_par = ')'; 
dot = '.';  

//keywords 
kclass = 'class'; 
kmethod = 'method'; 
koverride = 'override'; 
kextends = 'extends'; 
kfield = 'field'; 
kif = 'if'; 
kthen = 'then'; 
kelse = 'else'; 
knew = 'new';
kthis = 'this'; 
ksuper = 'super'; 
kis = 'is'; 
kerror = 'error';
kabstract = 'abstract';  
kinstanceof = 'instanceof' ; 
true = 'true';  
false = 'false';
kas = 'as'; 
//end of keywords 
 
line_comment = '//' nonl* ;
comment = '/*' nostar* '*'+ (not_star_slash nostar* '*'+)* '/';


number = digit+; 
id = letter (letter |digit) * ;


Ignored Tokens
     line_comment, comment, white_space;     

Productions 

start = e ; 
e = e plus  l_par [cenas]:e 
	| {cenas} kis ;  
	