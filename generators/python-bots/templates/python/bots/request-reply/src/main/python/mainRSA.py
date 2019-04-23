# pip install -r requirements.txt
#sudo npm link to test generator locally
from sym_api_client_python.configure.configure import SymConfig
from sym_api_client_python.auth.rsa_auth import SymBotRSAAuth
from sym_api_client_python.clients.SymBotClient import SymBotClient
from roomListenerImp import RoomListenerTestImp
from IMListenerImp import IMListenerTestImp
#debug logging --> set to debug --> check logs/example.log
import logging
logging.basicConfig(filename='example.log', format='%(asctime)s - %(name)s - %(levelname)s - %(message)s', filemode='w', level=logging.DEBUG)
logging.getLogger("urllib3").setLevel(logging.WARNING)
#main() acts as executable script --> run python3 hello.py to start Bot...

#adjust global variable below to auth either using RSA or certificates

def main():
        print('hi')
        #RSA Auth flow:
        configure = SymConfig('../resources/configRSA.json')
        configure.loadFromRSA()
        auth = SymBotRSAAuth(configure)
        auth.authenticate()
        #initialize SymBotClient with auth and configure objects
        botClient = SymBotClient(auth, configure)
        #initialize datafeed service
        DataFeedEventService = botClient.getDataFeedEventService()
        #initialize listener classes and append them to DataFeedEventService class
        #these listener classes sit in DataFeedEventService class as a way to easily handle events
        #coming back from the DataFeed

        imListenerTest = IMListenerTestImp(botClient)
        DataFeedEventService.addIMListener(imListenerTest)
        roomListenerTest = RoomListenerTestImp(botClient)
        DataFeedEventService.addRoomListener(roomListenerTest)
        #create data feed and read datafeed recursively
        print('starting datafeed')
        DataFeedEventService.startDataFeed()

if __name__ == "__main__":
    main()
