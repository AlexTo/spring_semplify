import React, {useEffect} from 'react';
import PropTypes from 'prop-types';
import {makeStyles} from '@material-ui/core/styles';
import TreeView from '@material-ui/lab/TreeView';
import TreeItem from '@material-ui/lab/TreeItem';
import {Typography, Button, Checkbox} from '@material-ui/core';
import Label from '@material-ui/icons/Label';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';
import clsx from "clsx";

const useTreeItemStyles = makeStyles(theme => ({
  root: {
    color: theme.palette.text.secondary,
    '&:focus > $content': {
      backgroundColor: `var(--tree-view-bg-color, ${theme.palette.grey[400]})`,
      color: 'var(--tree-view-color)',
    },
  },
  content: {
    color: theme.palette.text.secondary,
    borderTopRightRadius: theme.spacing(2),
    borderBottomRightRadius: theme.spacing(2),
    paddingRight: theme.spacing(1),
    fontWeight: theme.typography.fontWeightMedium,
    '$expanded > &': {
      fontWeight: theme.typography.fontWeightRegular,
    },
  },
  group: {
    marginLeft: 0,
    '& $content': {
      paddingLeft: theme.spacing(2),
    },
  },
  expanded: {},
  label: {
    fontWeight: 'inherit',
    color: 'inherit',
  },
  labelRoot: {
    display: 'flex',
    alignItems: 'center',
  },
  labelIcon: {
    marginRight: theme.spacing(1),
  },
  labelText: {
    fontWeight: 'inherit',
    flexGrow: 1,
    textTransform: 'capitalize'
  },
}));

function StyledTreeItem(props) {
  const classes = useTreeItemStyles();
  const {onCheckboxChange, checked, indeterminate, nodeId, labelText, labelIcon: LabelIcon, labelInfo, color, bgColor, ...other} = props;

  return (
    <TreeItem
      nodeId={nodeId}
      label={
        <div className={classes.labelRoot}>
          <Checkbox
            checked={checked}
            indeterminate={indeterminate}
            onChange={onCheckboxChange}
            onClick={e => (e.stopPropagation())}/>
          <Typography variant="body2" className={classes.labelText}>
            {labelText}
          </Typography>
          <Typography variant="caption" color="inherit">
            {labelInfo}
          </Typography>
        </div>
      }
      style={{
        '--tree-view-color': color,
        '--tree-view-bg-color': bgColor,
      }}
      classes={{
        root: classes.root,
        content: classes.content,
        expanded: classes.expanded,
        group: classes.group,
        label: classes.label,
      }}
      {...other}
    />
  );
}

StyledTreeItem.propTypes = {
  bgColor: PropTypes.string,
  color: PropTypes.string,
  labelIcon: PropTypes.elementType.isRequired,
  labelInfo: PropTypes.any,
  labelText: PropTypes.string.isRequired,
};

const useStyles = makeStyles((theme) => ({
  root: {},
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    marginBottom: theme.spacing(2)
  },
  actions: {
    display: 'flex',
    alignItems: 'center'
  },
  title: {
    position: 'relative',
    '&:after': {
      position: 'absolute',
      bottom: -8,
      left: 0,
      content: '" "',
      height: 3,
      width: 48,
      backgroundColor: theme.palette.primary.main
    }
  }
}));

export default function Buckets({buckets, onBucketCheckboxChange, onSubBucketCheckBoxChange, onApplyFilter}) {
  const classes = useStyles();

  return (
    <div className={clsx(classes.root)}>
      <div className={classes.header}>
        <Typography
          className={classes.title}
          variant="h5">
          Refine
        </Typography>
        <div className={classes.actions}>
          <Button variant="contained"
                  onClick={onApplyFilter}>Apply Filter</Button>
        </div>
      </div>
      <TreeView
        className={classes.root}
        defaultExpanded={buckets.filter(b => b.checked || b.indeterminate).map(b => b.uri)}
        defaultCollapseIcon={<ArrowDropDownIcon/>}
        defaultExpandIcon={<ArrowRightIcon/>}
        defaultEndIcon={<div style={{width: 24}}/>}
        onChange={() => {
        }}>
        {buckets.map(bucket =>
          <StyledTreeItem
            key={bucket.uri}
            nodeId={bucket.uri}
            labelText={bucket.name}
            //labelInfo={bucket.docCount}
            labelIcon={Label}
            checked={bucket.checked}
            indeterminate={bucket.indeterminate}
            onCheckboxChange={() => onBucketCheckboxChange(bucket.uri)}>
            {bucket.buckets.map(subBucket =>
              <StyledTreeItem
                key={subBucket.uri}
                nodeId={subBucket.uri}
                labelText={subBucket.name}
                labelInfo={subBucket.docCount}
                labelIcon={Label}
                checked={subBucket.checked}
                indeterminate={false}
                onCheckboxChange={() => onSubBucketCheckBoxChange(bucket.uri, subBucket.uri)}
              />)}
          </StyledTreeItem>
        )}
      </TreeView>
    </div>
  );
}
