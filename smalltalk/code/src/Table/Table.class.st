"
or: 33563697
"
Class {
	#name : #Table,
	#superclass : #Object,
	#instVars : [
		'values',
		'outFile',
		'header'
	],
	#category : #Table
}

{ #category : #initialization }
Table >> clearOut [
	"comment stating purpose of instance-side method"
	"scope: class-variables  &  instance-variables"	
	| s o |
	
	o := (outFile asFileReference) writeStream.
	o truncate.
	
	s := ('stdout.txt' asFileReference) writeStream.
	s truncate.
]

{ #category : #accessing }
Table >> getHeader [
	"comment stating purpose of instance-side method"
	"scope: class-variables  &  instance-variables"	
	^header.
]

{ #category : #accessing }
Table >> getVals [
	^values.
]

{ #category : #initialization }
Table >> initialize [
	values := OrderedCollection new.
]

{ #category : #printing }
Table >> printCols: cols [ 
	
	| mtxCol |
	
	cols do: [ :col |
		[mtxCol := (col asNumber) + 1] on: Error do: [ self error: 'COL INDEX ERROR'. ^-1]. 
		((mtxCol < 1) | (mtxCol > ((values at:1) size)))
		ifTrue: [ 
			self printError: 'COL INDEX ERROR'.
			^-1.
		].
	].
	
	(outFile asFileReference) writeStreamDo: [ :s |
		header ifNotNil: [
			cols do: [ :col | 
				mtxCol := (col asNumber) + 1.
				s << (header at: mtxCol); << String space.
			].
			s << String lf.
		].
		values do: [ :row |
			cols do: [ :col |
				mtxCol := (col asNumber) + 1.
				s << (row at: mtxCol); << String space.
			].
			s << String lf.
		].
	].
	^0.
]

{ #category : #printing }
Table >> printError: error [
	"comment stating purpose of instance-side method"
	"scope: class-variables  &  instance-variables"	
			
	('stdout.txt' asFileReference) writeStreamDo: [ :s | s << error; << Character lf].
]

{ #category : #initialization }
Table >> readTable: tableFile header: aBool [
	"comment stating purpose of instance-side method"
	"scope: class-variables  &  instance-variables"	
	
	| g rSize |
	
	rSize := 0.
	(tableFile asFileReference) contents lines do: [ :each |
		g := [ :j | j isSeparator ]	split: each.
		((aBool) & (rSize = 0)) 
		ifTrue: [ 
		 	header := g.
			rSize := g size.
		]
		ifFalse: [
			((rSize ~= 0) & (rSize ~= (g size))) ifTrue: [
				self printError: 'COL SIZE ERROR'.
				^-1.
			].
			rSize := g size.
			values add: g.
		].
	].
	^0
]

{ #category : #initialization }
Table >> setOutFile: setFile [
	"comment stating purpose of instance-side method"
	"scope: class-variables  &  instance-variables"	
			
	outFile := setFile.
]

{ #category : #arithmetic }
Table >> sumCol: col [
	"sums all values in a given column"	
	| sum mtxCol |
	
	[mtxCol := (col asNumber) + 1] on: Error do: [self printError: 'COL INDEX ERROR'].
	((mtxCol < 1) | ((mtxCol) > (values size))) ifTrue: [
		self printError: 'COL INDEX ERROR'.
		^-1.
	].

	sum := 0.
	values do: [ :row |
		[sum := ((row at: mtxCol) asNumber) + sum] on: Error do: [ 
			self printError: 'TYPE ERROR'.
			^-1.
		].
	].
	
	
	(outFile asFileReference) writeStreamDo: [ :s |
		header ifNotNil: [ s << (header at: mtxCol); << String lf ].
		s << sum asString.
		s << String lf.
	].
	^0.
]
