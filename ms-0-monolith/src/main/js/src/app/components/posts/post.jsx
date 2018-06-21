import './post.scss';
import React from 'react';

export const Post = ({ innerHTML }) => <div
  className={'posts'}
  dangerouslySetInnerHTML={{__html: innerHTML}}
/>;
