import HelloWorld from './ToolHelloWorld.vue'
import {RouterPlugin} from '@dokit/web-core'

export default new RouterPlugin({
  nameZh: '任意门',
  name: 'h5-door',
  icon: 'https://pt-starimg.didistatic.com/static/starimg/img/FHqpI3InaS1618997548865.png',
  component: HelloWorld
})

