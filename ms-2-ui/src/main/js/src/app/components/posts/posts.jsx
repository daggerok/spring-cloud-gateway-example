import React, { Component } from 'react';
import { Post } from './post';

const { posts: paths } = require('../../../posts');

export class Posts extends Component {
  constructor(props) {
    super(props);
    this.state = {
      posts: [],
    };
  }

  componentDidMount() {
    paths.forEach(path => {
      import('../../../posts' + path)
        .then(data => data.default)
        .then(html => this.setState({
          posts: [
            html,
            ...this.state.posts,
          ],
        }));
    });
  }

  render() {
    return <div>
      {
        this.state.posts.map((innerHTML, key) =>
          <Post key={key} innerHTML={innerHTML}/>
        )
      }
    </div>
  }
}
