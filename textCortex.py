# import requests
# import json
#
# url = "https://api.textcortex.com/hemingwai/generate_text_v3/"
# api_key = ""
#
# original_sentence = ""
# temperature = 1.3
# source_language = "en"
#
# def setPayload(original, temp, lang):
#     original_sentence = original
#     temperature = temp
#     source_language = lang
#
# payload = json.dumps({
#     "template_name": "paraphrase",
#     "prompt": {
#         "original_sentence": original_sentence
#     },
#     "temperature": temperature,
#     "token_count": 20,
#     "n_gen": 4,
#     "source_language": source_language,
#     "api_key": api_key
# })
# headers = {
#     'Content-Type': 'application/json'
# }
#
# response = requests.request("POST", url, headers=headers, data=payload)
#
# print(response.text)
