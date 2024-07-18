import request from '@/utils/request'

export function getRandomQuestion() {
  return request({
    url: '/sys/chat/getRandomQuestion',
    method: 'get',
  })
}

export function getSessionHistoryQA(sessionId) {
  return request({
    url: '/sys/chat/getSessionHistoryQA/'+sessionId,
    method: 'get',
  })
}

export function getUserSessionList() {
  return request({
    url: '/sys/chatSession',
    method: 'get',
  })
}