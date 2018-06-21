import React, { Component } from 'react';

export class Contents extends Component {
  constructor(props) {
    super(props);
    this.state = {
      contents: [],
    };
  }

  componentDidMount() {
    fetch('http://127.0.0.1:8080/api/contents', { method: 'get' })
      .then(data => console.log(data.json()));
  }

  render() {
    return <div>
      {
        this.state.contents.map((innerHTML, key) =>
          <div key={key} dangerouslySetInnerHTML={{__html: innerHTML}}/>
        )
      }
    </div>
  }
}
