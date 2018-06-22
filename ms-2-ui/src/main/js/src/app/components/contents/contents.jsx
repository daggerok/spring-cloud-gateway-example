import React, { Component } from 'react';

export class Contents extends Component {
  constructor(props) {
    super(props);
    this.state = {
      contents: [],
    };
  }

  componentDidMount() {
    fetch('/api/contents', { method: 'get' })
      .then(data => data.json())
      .then(json => this.setState({
        ...this.state,
        contents: [
          ...this.state.contents,
          ...json,
        ],
      }))
    ;
  }

  render() {
    return <div>
      {
        this.state.contents.map((json, key) =>
          <pre key={key}>
            <code className={'json'}
                  dangerouslySetInnerHTML={{__html: JSON.stringify(json, null, 2)}}/>
          </pre>
        )
      }
    </div>
  }
}
