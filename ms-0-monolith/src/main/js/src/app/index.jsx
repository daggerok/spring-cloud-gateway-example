import './index.styl';
import React from 'react';
import { render } from 'react-dom';
import { BrowserRouter, Link, Redirect, Route, Switch } from 'react-router-dom';
import { Posts } from './components/posts/posts';
import { Contents } from './components/contents/contents';

const dynamicPostsLoad = () => {
/*
  import(/!* webpackChunkName: 'blog' *!/ '../posts')
    .then(({ posts }) => posts)
    .then(paths => {
      paths.map(path => {
        import(`../posts${path}`)
          .then(it => it)
          .then(it => console.log('it', it.default))
        ;
      });
    });
  */
};

const Index = () => <div onClick={dynamicPostsLoad} className='container'>
  <div>
    <img src={require('./index.png')}/>
  </div>
  <h1>Миу... котег светится!</h1>
  <Contents/>
  <Posts/>
</div>;

const Routes = () => <BrowserRouter basename={process.env.BASE_HREF || ''}>
  <div>
    <div className='nav'>
      <Link to='/'>Home</Link>
    </div>
    <Switch>
      {/*<Route exact path='/home' component={App}/>
         <Redirect exact path='/' to='/home'/>*/}
      <Route component={Index}/>
    </Switch>
  </div>
</BrowserRouter>;

render(
  <Routes/>,
  document.getElementById('app')
);
