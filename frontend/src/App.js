import React from 'react';
import logo from './components/echoLogoBlack.jpg';
import './App.css';

import Header from './components/Header';

function App() {
  return (
    <div className="App">
      <Header logoSrc={logo} />
    </div>
  );
  }
  export default App;
