#!/usr/bin/python
# coding: utf8
import pandas as pd

class fangYao():
	def __init__(self):
		self.data = pd.read_excel('fangYao.xlsx')
		self.data['脉象'] = self.data['脉象'].apply(lambda x: x.split())
		self.data['中药'] = self.data['中药'].apply(lambda x: x.split())
	def match_symptom( self, symptoms ):
		if not symptoms:
			return( {} )
		tmp_result = self.data
		for i in range( len(symptoms['症状']) ):
			tmp_result = tmp_result.loc[ (tmp_result['症状1'] == symptoms['症状'][i]) | (tmp_result['症状2'] == symptoms['症状'][i])
							| (tmp_result['症状3'] == symptoms['症状'][i]) | (tmp_result['症状4'] == symptoms['症状'][i])]	
		for i in range( len(symptoms['舌象']) ):
			tmp_result = tmp_result.loc[ tmp_result['舌象'] == symptoms['舌象'][i] ]
		for i in range( len(symptoms['脉象']) ):
			truth = list(tmp_result['脉象'].apply( lambda x: symptoms['脉象'][i] in x))
			tmp_result = tmp_result.loc[ truth ]
		if len(tmp_result) == 0:
			return( {} )
		elif len(tmp_result) == 1:
			result = {'证型':list(tmp_result['证型'])[0],'方剂':list(tmp_result['方剂'])[0],'中药':list(tmp_result['中药'])[0]}
		else:
			possible_symptoms_raw = list(tmp_result['症状1'])+list(tmp_result['症状2'])+list(tmp_result['症状3'])+list(tmp_result['症状4'])
			possible_symptoms = list( set(possible_symptoms_raw) - set(symptoms['症状']) )
			result = {'症状':possible_symptoms, '舌象':list(tmp_result['舌象']),'脉象':list(tmp_result['脉象']) }
		return(result)

	def test( self ):
		symptom = {'症状':["两眼干涩,视物模糊", "肢麻关节屈伸困难"], '脉象':["细脉"], '舌象':[]}
		print(self.match_symptom(symptom))