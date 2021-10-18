"
Class for running the table with command line input.
"
Class {
	#name : #TableMain,
	#superclass : #Object,
	#instVars : [
		't'
	],
	#category : #Table
}

{ #category : #initialization }
TableMain >> initialize [
	"comment stating purpose of instance-side method"
	"scope: class-variables  &  instance-variables"
	t := Table new.
]

{ #category : #run }
TableMain >> main: args [
	"runs the table program"
	
	| argv head cmdIndex |
	
	argv := [ :j | j isSeparator ]	split: args.
		
	cmdIndex := 1.
	head := ((argv at: 1) = '-header').
	head ifTrue: [ cmdIndex := 2 ].
	((t readTable: (argv at: (2 + cmdIndex)) header: head) = -1) ifTrue: [ ^-1 ].
	t setOutFile: (argv at: (3 + cmdIndex)).
	t clearOut.
	
	((argv at: cmdIndex) = '-print') ifTrue: [
		| g |
		g := ',' split: (argv at: (1 + cmdIndex)).
		((t printCols: g) = -1) ifTrue: [ ^-1 ].
	].
	
	((argv at: cmdIndex) = '-sum') ifTrue: [ 
		((t sumCol: (argv at: (1+cmdIndex))) = -1) ifTrue: [ ^-1 ].
	].
]
